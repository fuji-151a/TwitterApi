package twitter.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * TwtitterStreamingApi.
 * @author yuya
 *
 */
class TwitterStreaming {
    /** consumerKey. */
    private static String consumerKey;
    /** cosumerSecret. */
    private static String consumerSecret;
    /** accessToken 初期値. */
    private static String accessToken = null;
    /** accessTokenSecret 初期値. */
    private static String accessTokenSecret = null;
    /** json形式で保存するか. */
    private static boolean jsonStoreEnabled;

    /**
     * SetUp API Key.
     * @param fileName PropertiesFile
     * @throws IOException
     */
    TwitterStreaming(final String fileName)
            throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(fileName));
        consumerKey = prop.getProperty("oauth.consumerKey");
        consumerSecret = prop.getProperty("oauth.consumerSecret");
        accessToken = prop.getProperty("oauth.accessToken");
        accessTokenSecret = prop.getProperty("oauth.accessTokenSecret");
        jsonStoreEnabled = Boolean.valueOf(prop.getProperty("jsonStoreEnabled"));
    }

    /**
     * @param args PropetiesFile
     * @throws IOException
     * @throws TwitterException.
     */
    public static void main(String[] args) throws TwitterException, IOException {
        if (args.length != 1) {
            System.out.println("Please input format: "
                    + "java -cp jarFiles "
                    + "twitter.twitterUserTimeLine PropertiesFile");
            System.out.println("args[0]:twitter4j.properties");
            System.exit(3);
        }
        String filePath = "src/main/resources/twitter4j.properties";
        new TwitterStreaming(filePath);
        // SetUp AccessToken
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
                .setJSONStoreEnabled(jsonStoreEnabled)
                .build();
        // TwitterStreamのインスタンス作成
        TwitterStreamFactory factory = new TwitterStreamFactory(conf);
        TwitterStream twitterStream = factory.getInstance();

        // Listenerを登録
        twitterStream.addListener(new Listener());

        // 実行
        twitterStream.sample();
    }
}