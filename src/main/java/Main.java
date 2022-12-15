import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main  {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
        Keyboard keyboard = new Keyboard();

        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Привет")));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Кто я?")));

        allKey.add(line1);
        keyboard.setButtons(allKey);
        GroupActor actor = new GroupActor(217778930,"vk1.a.yWAeCU95Lr4qZNKR1uJRA4VVce_D5Sdl4vhBATvpT99WAYZ-E6s7w74f79MM2f82t2hf5LmtWGGGbzGEwdlvpBD5j-fiCLGRYbkULBt2ZPH_jzcwGI7VVqJGjW5TVHG7ho9EmzQrO8R3LBCnfEF_CYowGQahUVe8lxvqN_WiJrbSgWXWvXlX1lrn_B4TeU22wHWrEaOF5HkXr7YIBtTHhA");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        while (true){
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if(!messages.isEmpty()){
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    try {
                        if(message.getText().equals("Привет")){
                            vk.messages().send(actor).message("Привет!").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                       else if(message.getText().equals("Кто я?")){
                            vk.messages().send(actor).message("Ты хороший человек.!").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
//                        else if(message.getText().equals("Кнопки")){
//                            vk.messages().send(actor).message("А вот и они").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
//                        }
                       else {
                            vk.messages().send(actor).message("Я тебя не понял").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }

                    }
                    catch (ApiException | ClientException e){e.printStackTrace();}
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}
