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
        new TwitterStreaming(args[0]);
        // SetUp AccessToken
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
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

/** Tweetを出力するだけのListener. */
class Listener extends StatusAdapter {
    // Tweetを受け取るたびにこのメソッドが呼び出される
    @Override
    public void onStatus(Status status) {
        if (isJapanese(status.getText())) {
            String text = normalizeText(status.getText());
            System.out.printf("%d\t%s\t%s\t%s\n",
                    status.getId(),
                    status.getUser().getScreenName(),
                    text,
                    status.getCreatedAt());
        }
    }

    /**
     * 日本語のみのフィルタリングを行う.
     * @param text : tweet
     * @return boolean
     */
    public boolean isJapanese(String text) {
        Matcher m = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}])")
                .matcher(text);
        return m.find();
    }

    /**
     *  ツイートに改行が含まれていた場合は半角空白文字に置き換える.
     * @param s : tweet
     * @return Replace String
     */
    private static String normalizeText(String s) {
          // to space
          s = s.replaceAll("\r\n", "\n");
          s = s.replaceAll("\n", " ");
          return s;
    }
}