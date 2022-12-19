import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.appnews.GetNewsForApp;
import com.lukaspradel.steamapi.data.json.appnews.Newsitem;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetNewsForAppRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
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
    private static final int Number = 914;
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        //STEAT
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("ID").build();
        GetNewsForAppRequest request = SteamWebApiRequestFactory.createGetNewsForAppRequest(570); // appId of Dota 2
        GetNewsForApp getNewsForApp;
        try {
            getNewsForApp = client.<GetNewsForApp> processRequest(request);
        } catch (SteamApiException e) {
            throw new RuntimeException(e);
        }
        //VK
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
                        else if(message.getText().equals("Dota")){
                            StringBuilder stringBuilder = new StringBuilder();
//                            StringBuilder stringBuilder1 = null;
//                            StringBuilder stringBuilder2= null;
                            int temp = 0;
                            int countLength=0;
                            int count = 0;
                            getNewsForApp.getAppnews().getNewsitems().stream().map(Newsitem::getContents).forEach(stringBuilder::append);
//                            if (stringBuilder.length()>900){
//                                stringBuilder1 = new StringBuilder();
//                                stringBuilder2 = new StringBuilder();
//                                stringBuilder1.append(stringBuilder.substring(0,900));
//                                stringBuilder2.append(stringBuilder.substring(900,stringBuilder.length()));
//                            }
//                            vk.messages().send(actor).message(String.valueOf(stringBuilder1)).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
//                            vk.messages().send(actor).message(String.valueOf(stringBuilder2)).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                            if(stringBuilder.length()<914){
                                vk.messages().send(actor).message(String.valueOf(stringBuilder)).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                            }
                            else {
                                String str;
                                System.out.println("Размер: " +stringBuilder.length());
                                while (true){
                                    if(temp == stringBuilder.length()-1){
                                        break;
                                    }
                                   else if(count == 0){
                                         str =stringBuilder.substring(0,Number);
                                        temp = 914;
                                        count++;
                                    }
                                    else {
                                        countLength =(stringBuilder.length()-1)- (stringBuilder.length()-1-temp);// 999-86
                                        System.out.println(temp);
                                        System.out.println(temp+countLength);
                                         str =stringBuilder.substring(temp,temp+countLength);
                                        temp = temp+ countLength;
                                    }
                                    vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                                }
                            }
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
