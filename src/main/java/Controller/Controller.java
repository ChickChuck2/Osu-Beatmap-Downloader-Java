package Controller;

import Config.createConfig;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;

public class Controller {

    public AnchorPane advancedSearch;

    public Button ConfirmPath;
    public TextField OsuDirectory;

    public Button DownloadButton;

    public ComboBox<String> genre;
    public ComboBox<String> language;

    public TextField T_minAr;
    public TextField T_maxAr;

    public TextField T_minOD;
    public TextField T_maxOD;

    public TextField T_minCS;
    public TextField T_maxCS;

    public TextField T_minHP;
    public TextField T_maxHP;

    public TextField T_minDIFF;
    public TextField T_maxDIFF;

    public TextField T_minBPM;
    public TextField T_maxBPM;

    public TextField T_minLENGHT;
    public TextField T_maxLENGHT;

    public ProgressBar DownlaodPbar;
    public CheckBox CheckAdvancedFilter;

    public CheckBox C_STD;
    public CheckBox C_Catch;
    public CheckBox C_Mania;
    public CheckBox C_Taiko;

    public CheckBox S_Qualified;
    public CheckBox S_Aprove;
    public CheckBox S_UnRanqued;
    public CheckBox S_Ranked;

    int Ioffset = 0;

    @FXML
    public void initialize() {
        File Config = new File("Config.json");
        try{
            if (Config.exists()) {
                LoadConfig();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        advancedSearch.setDisable(true);


        //Genre
        genre.getItems().add("Any"); //0
        genre.getItems().add("Unspecified"); //1
        genre.getItems().add("Game"); //2
        genre.getItems().add("Anime"); //3
        genre.getItems().add("Rock"); //4
        genre.getItems().add("Pop"); //5
        genre.getItems().add("Other"); //6
        genre.getItems().add("Novelty"); //7
        genre.getItems().add("HipHop"); //8
        genre.getItems().add("Eletronic"); //10

        genre.getSelectionModel().select(0);

        //Language
        language.getItems().add("Any"); //0
        language.getItems().add("Other"); //1
        language.getItems().add("English"); //2
        language.getItems().add("Japanese"); //3
        language.getItems().add("Chinese"); //4
        language.getItems().add("Instrumental"); //5
        language.getItems().add("Korean"); //6
        language.getItems().add("French"); //7
        language.getItems().add("German"); //8
        language.getItems().add("Swedish"); //9
        language.getItems().add("Spanish"); //10
        language.getItems().add("Italian"); //11

        language.getSelectionModel().select(0);


         Thread thread = new Thread(() -> {
             while (!Thread.currentThread().isInterrupted()) {
                 try {
                     Thread.sleep(500);
                     advancedSearch.setDisable(!CheckAdvancedFilter.isSelected());

                 } catch (Exception exception) {
                     exception.printStackTrace();
                 }
             }
         });
         thread.start();
    }

    public void GetBeatmapInfo() {
        String minARString = T_minAr.getText();
        String maxARString = T_maxAr.getText();

        String minOdString = T_minOD.getText();
        String maxOdString = T_maxOD.getText();

        String minCsString = T_minCS.getText();
        String maxCsString = T_maxCS.getText();

        String minHpString = T_minHP.getText();
        String maxHpString = T_maxHP.getText();

        String minDiffString = T_minDIFF.getText();
        String maxDiffString = T_maxDIFF.getText();

        String minBPMString = T_minBPM.getText();
        String maxBPMString = T_maxBPM.getText();

        String minLenghtString = T_minLENGHT.getText();
        String maxLenghtString = T_maxLENGHT.getText();

        int minAr = Integer.parseInt(minARString);
        int maxAr = Integer.parseInt(maxARString);

        int minOd = Integer.parseInt(minOdString);
        int maxOd = Integer.parseInt(maxOdString);

        int minCs = Integer.parseInt(minCsString);
        int maxCs = Integer.parseInt(maxCsString);

        int minHp = Integer.parseInt(minHpString);
        int maxHp = Integer.parseInt(maxHpString);

        int minDiff = Integer.parseInt(minDiffString);
        int maxDiff = Integer.parseInt(maxDiffString);

        int minBPM = Integer.parseInt(minBPMString);
        int maxBPM = Integer.parseInt(maxBPMString);

        int minLenght = Integer.parseInt(minLenghtString);
        int maxLenght = Integer.parseInt(maxLenghtString);

        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try{
                    boolean modeSTD = C_STD.isSelected();
                    boolean modeCatch = C_Catch.isSelected();
                    boolean modeMania = C_Mania.isSelected();
                    boolean modeTaiko = C_Taiko.isSelected();

                    boolean Ranked = S_Ranked.isSelected();
                    boolean Aprove = S_Aprove.isSelected();
                    boolean UnRanked = S_UnRanqued.isSelected();
                    boolean Qualified = S_Qualified.isSelected();

                    //Mode
                    String modes = "";

                    if (modeSTD) {
                        modes +="mode=0&"; //0
                    }
                    if (modeTaiko) {
                        modes += "mode=1&";
                    }
                    if (modeCatch) {
                        modes += "mode=2&";
                    }
                    if (modeMania) {
                        modes += "mode=3&"; //3
                    }
                    if (modes.length() == 0) {
                        modes = "";
                    } else  {
                        modes = modes.substring(0, modes.length() -1);
                    }

                    //Status
                    String status = "";

                    if (UnRanked) {
                        status += "status=0&";
                    }
                    if (Ranked) {
                        status += "status=1&";
                    }
                    if (Aprove) {
                        status += "status=2&";
                    }
                    if (Qualified) {
                        status += "status=3&";
                    }
                    if (status.length() == 0) {
                        status = "";
                    } else {
                        status = status.substring(0, status.length() -1);
                    }

                    String minar = "";
                    if (minAr > 0) {
                        minar += String.format("min_ar=%s&", minAr);
                    }
                    if (minar.length() == 0) {
                        minar = "";
                    }

                    String maxar = "";
                    if (maxAr > 0) {
                        maxar += String.format("max_ar%s&", maxAr);
                    }
                    if (maxar.length() == 0) { maxar = "";
                    }

                    String minod = "";
                    if (minOd > 0) {
                        minod += String.format("min_od=%s&",minOd);
                    }
                    if (minod.length()== 0) {
                        minod = "";
                    }

                    String maxod = "";
                    if (maxOd > 0) {
                        maxod += String.format("max_od=%s&",maxOd);
                    }
                    if (maxod.length() == 0) {
                        maxod = "";
                    }

                    String mincs = "";
                    if (minCs > 0) {
                        mincs += String.format("min_cs=%s&",minCs);
                    }
                    if (mincs.length() == 0) {
                        mincs = "";
                    }

                    String maxcs = "";
                    if (maxCs > 0) {
                        maxcs = String.format("max_cs=%s&", maxCs);
                    }
                    if (maxcs.length() == 0) {
                        maxcs = "";
                    }

                    String minhp = "";
                    if(minHp > 0) {
                        minhp += String.format("min_hp=%s&", minHp);
                    }
                    if (minhp.length() == 0) {
                        minhp = "";
                    }

                    String maxhp = "";
                    if (maxHp > 0) {
                        maxhp += String.format("max_hp=%s&", maxHp);
                    }
                    if (maxhp.length() == 0) {
                        maxhp = "";
                    }

                    String mindiff = "";
                    if (minDiff > 0) {
                        mindiff += String.format("min_diff=%s&", minDiff);
                    }
                    if (mindiff.length() == 0) {
                        mindiff = "";
                    }

                    String maxdiff = "";
                    if (maxDiff > 0) {
                        maxdiff += String.format("max_diff=%s&", maxDiff);
                    }
                    if (maxdiff.length() == 0) {
                        maxdiff = "";
                    }

                    String minbpm = "";
                    if (minBPM > 0) {
                        minbpm += String.format("min_bpm=%s&", minBPM);
                    }
                    if (minbpm.length() == 0) {
                        minbpm = "";
                    }

                    String maxbpm = "";
                    if (maxBPM > 0) {
                        maxbpm += String.format("max_bpm=%s&", maxBPM);
                    }
                    if (maxbpm.length() == 0) {
                        maxbpm = "";
                    }

                    String minlenght = "";
                    if (minLenght > 0) {
                        minlenght += String.format("min_length=%s&", minLenght);
                    }
                    if (minlenght.length() == 0) {
                        minlenght = "";
                    }

                    String maxlenght = "";
                    if (maxLenght > 0) {
                        maxlenght += String.format("max_length=%s&", maxLenght);
                    }
                    if (maxlenght.length() == 0) {
                        maxlenght = "";
                    }

                    int DownloadGenre = genre.getSelectionModel().getSelectedIndex();
                    int DownloadlANGUAGE = language.getSelectionModel().getSelectedIndex();

                    String language = "";
                    if (DownloadlANGUAGE > 0) {
                        language += String.format("language=%s&", DownloadlANGUAGE);
                    }
                    if (language.length() == 0) {
                        language = "";
                    }

                    String genre = "";
                    if (DownloadGenre > 0) {
                        genre += String.format("genre=%s&", DownloadGenre);
                    }
                    if (genre.length() == 0) {
                        genre = "";
                    }

                    Ioffset ++;

                    String offset = String.format("offset=%s", Ioffset);

                    String url = "https://api.chimu.moe/v1/search?" +

                            String.format("%s&", offset) +

                            String.format("%s&", status) +

                            String.format("%s&", modes) +

                            String.format("%s", minar) +
                            String.format("%s", maxar) +

                            String.format("%s", minod) +
                            String.format("%s", maxod) +

                            String.format("%s", mincs) +
                            String.format("%s", maxcs) +

                            String.format("%s", minhp) +
                            String.format("%s", maxhp) +

                            String.format("%s", mindiff) +
                            String.format("%s", maxdiff) +

                            String.format("%s", minbpm) +
                            String.format("%s", maxbpm) +

                            String.format("%s", minlenght) +
                            String.format("%s", maxlenght) +

                            String.format("%s", genre) +

                            String.format("%s", language);

                    System.out.println(url);

                    URL obj = new URL(url);

                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    // optional default is GET
                    con.setRequestMethod("GET");
                    //add request header
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    //Read JSON response and print
                    JSONObject myResponse = new JSONObject(response.toString());

                    JSONArray dataArray = new JSONArray(myResponse.get("data").toString());
                    int lendataObject = dataArray.length();
                    Random dataRandom = new Random();
                    int RandomData = dataRandom.nextInt(lendataObject);
                    JSONObject randomDataArray = new JSONObject(dataArray.get(RandomData).toString());

                    JSONArray ArrayChildren = new JSONArray(randomDataArray.get("ChildrenBeatmaps").toString());
                    int lenChildrenArray = ArrayChildren.length();
                    Random ChildrenRandom = new Random();
                    int RandomChildren = ChildrenRandom.nextInt(lenChildrenArray);

                    JSONObject Beatmap = new JSONObject(ArrayChildren.get(RandomChildren).toString());

                    //FileName
                    String BeatmapName = Beatmap.getString("OsuFile").replace(".osu","");

                    //Beatmap ID
                    String ParentSetIdstr = Beatmap.get("ParentSetId").toString();
                    int ParentSetId = Integer.parseInt(ParentSetIdstr);

                    //Action que vai fazer o download
                    Download(BeatmapName, ParentSetId);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void Download(String BeatmapName, int ParentSetId) {
        System.out.println(BeatmapName);
        try{
            DownlaodPbar.setProgress(50);
            URL url = new URL(String.format("https://api.chimu.moe/v1/download/%s?n=1", ParentSetId));
            URLConnection c = url.openConnection();
            c.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

            BufferedInputStream in = new BufferedInputStream(c.getInputStream());
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            FileOutputStream fileOS = new FileOutputStream(String.format("%s%s-%s.osz", OsuDirectory.getText(),ParentSetId, BeatmapName));

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOS.write(dataBuffer, 0, bytesRead);
            }

            DownlaodPbar.setProgress(100);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Algo deu errado porra");
        }

        DownlaodPbar.setProgress(0);
    }

    public void CreateConfig() {
        createConfig createConfig = new createConfig();
        createConfig.CreateConfig(OsuDirectory.getText());

        LoadConfig();
    }


    public void LoadConfig() {
        try {
            Gson gson = new Gson();

            Reader reader = Files.newBufferedReader(Paths.get("Config.json"));

            Map map = gson.fromJson(reader, Map.class);

            String GameFolder = map.get("GameFolder").toString();

            OsuDirectory.setText(GameFolder);
        }catch (Exception exception) {
            OsuDirectory.setPromptText("Primeiro, Crie um path com a pasta dos sons de Osu");
        }
    }
}