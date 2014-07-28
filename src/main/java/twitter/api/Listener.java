package twitter.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.json.DataObjectFactory;

/** Tweetを出力するだけのListener. */
public class Listener extends StatusAdapter {
    // Tweetを受け取るたびにこのメソッドが呼び出される
    @Override
    public void onStatus(Status status) {
        if (isJapanese(status.getText())) {
            String text = normalizeText(status.getText());
            System.out.println(DataObjectFactory.getRawJSON(status));
//            System.out.printf("%d\t%s\t%s\t%s\n",
//                    status.getId(),
//                    status.getUser().getScreenName(),
//                    text,
//                    status.getCreatedAt());
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