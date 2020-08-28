package com.o2o.action.server.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Service
public class DBConnector implements Serializable {
    String userEmail = "";

    String commandGetUser = "/getUser/";
    String commandCreateUser = "/createUser/";
    String commandGetHint = "/getHint/";
    String commandGetWord = "/getWord/";
    String commandGetTotalRank = "/getTotalRank";
    String commandGetMyRank = "/getMyRank/";
    String defaultSendUrl = "https://actions.o2o.kr/devsvr5";
    QueryController queryController;
    JsonParser jsonParser;
    JsonArray user;

    public DBConnector(String email) {
        userEmail = email;
        String userCheckUrl = defaultSendUrl + commandGetUser + userEmail;
        queryController = new QueryController();
        String result = queryController.get(userCheckUrl);
        System.out.println("result =" + result.length() + "result");

        jsonParser = new JsonParser();


        if(result.length() == 3){
            //처음사용자 등록
            String fisrtInputUrl = defaultSendUrl + commandCreateUser + userEmail;
            String createResult = queryController.get(fisrtInputUrl);
            System.out.println("createResult = " + createResult);
            DBConnector dbConnector = new DBConnector(userEmail);
        }else{
            //이미 등록된 사용
            //유저 한명 data 전체 row
             user = (JsonArray) jsonParser.parse(result);
            String userData = user.toString();
            System.out.println("user = " + userData);
            System.out.println(user.get(0).getAsJsonObject().size());
        }

    }

    //유저 각각 요소 가져오는거
    public String getUserLevel() {
        return user.get(0).getAsJsonObject().get("userLevel").toString();
    }
    public String getUserExp() {
        return user.get(0).getAsJsonObject().get("userExp").toString();
    }
    public String getUserCoin() {
        return user.get(0).getAsJsonObject().get("userCoin").toString();
    }
    public String getUserHint() {
        return user.get(0).getAsJsonObject().get("userHint").toString();
    }
//    public String setUserLevel(Short level, String email){
//
//    }


    public String[][] getTotalRank(){
        String getTotalRankUrl = defaultSendUrl + commandGetTotalRank;
        String getTotalRankResult = queryController.get(getTotalRankUrl);
        JsonArray totalRankArray = (JsonArray) jsonParser.parse(getTotalRankResult);
        List<JsonArray> totalRankList = Arrays.asList(totalRankArray);
        int rowSize = totalRankArray.size();
        int colSize = totalRankArray.get(0).getAsJsonObject().size();
        String[][] totalRank2X = new String[rowSize][2];
        for(int i = 0; i<rowSize; i++){
            totalRank2X[i][0] = totalRankArray.get(i).getAsJsonObject().get("userEmail").toString();
            System.out.print(totalRank2X[i][0]);
            totalRank2X[i][1] = totalRankArray.get(i).getAsJsonObject().get("userExp").toString();
            System.out.println(totalRank2X[i][1]);

        }


        return totalRank2X;
    }

    public int getMyRank(String email){
        String getMyRankUrl = defaultSendUrl + commandGetMyRank + email;
        String getMyRankResult = queryController.get(getMyRankUrl);
        System.out.println("myrank - " + jsonParser.parse(getMyRankResult).getAsInt());
        return jsonParser.parse(getMyRankResult).getAsInt();
    }


    public List<String> getWord(int stage, int difficulty){
        System.out.println(stage + " " + difficulty);
        ArrayList<String> wordList = new ArrayList<>();
        String sndn = "s"+stage+"d"+difficulty;
        String getWordUrl = defaultSendUrl + commandGetWord + sndn;
        String getWordResult = queryController.get(getWordUrl);
        JsonArray wordArray = (JsonArray) jsonParser.parse(getWordResult);
        int size = wordArray.size();
        for(int i = 0; i<size; i++){
            wordList.add(wordArray.get(i).getAsJsonObject().get("wordContent").toString());
        }

        return wordList;
    }

    public ArrayList<String> getHint(String word){
        ArrayList<String> hintList = new ArrayList<>();
        String getHintUrl = defaultSendUrl + commandGetHint + word;
        String getHintResult = queryController.get(getHintUrl);
        JsonArray hintArray = (JsonArray) jsonParser.parse(getHintResult);
        int size = hintArray.size();
        for(int i = 0; i<size; i++){
            hintList.add(hintArray.get(i).getAsJsonObject().get("hintContent").toString());
        }

        return hintList;
    }

}








