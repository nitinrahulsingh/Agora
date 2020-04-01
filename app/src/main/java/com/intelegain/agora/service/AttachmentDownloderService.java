package com.intelegain.agora.service;


import android.app.IntentService;
import android.content.Intent;

import android.util.Base64;


import androidx.annotation.Nullable;

import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.KnowdlegeAttachmentResponce;
import com.intelegain.agora.utils.Contants2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by kalpesh.c on 1/3/18.
 *
 * http://androidsrc.net/android-intentservice-working-advantages-using/
 *
 */

public class AttachmentDownloderService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param
     */

    public AttachmentDownloderService() {
        super(AttachmentDownloderService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String kbId = intent.getStringExtra(Contants2.KBID);
        String kbFileName = intent.getStringExtra(Contants2.KB_FILE_NAME);
        String empId = intent.getStringExtra(Contants2.EMP_ID);
        String token = intent.getStringExtra(Contants2.TOKEN);

        Map<String, String> params = new HashMap<String, String>();
        params.put("KbId", kbId);
        params.put("FileName", kbFileName);
        Retrofit retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Call<KnowdlegeAttachmentResponce> call = apiInterface.getKnowledgeAttachment(empId, token, params);

        call.enqueue(new Callback<KnowdlegeAttachmentResponce>() {
            @Override
            public void onResponse(Call<KnowdlegeAttachmentResponce> call, Response<KnowdlegeAttachmentResponce> response) {
                switch (response.code()) {
                    case 200:

                        KnowdlegeAttachmentResponce data=response.body();
                        writeFile(data.Attachments.get(0).FileName);

                        break;
                    case 403:
                        break;
                    case 500:

                        break;
                    default:
                }
            }
            @Override
            public void onFailure(Call<KnowdlegeAttachmentResponce> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private boolean writeFile(String FileData) {

        File folder = new File(AttachmentDownloderService.this.getFilesDir(), Contants2.agora_folder);
        if (!folder.exists())
            folder.mkdir(); // If directory not exist, create a new one
        File file = new File(folder, FileData);
        try {
            if (file.exists())
                file.createNewFile();
            byte[] stringBytes = Base64.decode(FileData, Base64.DEFAULT);
            FileOutputStream os = new FileOutputStream(file.getAbsoluteFile(), false);
            os.write(stringBytes);
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

