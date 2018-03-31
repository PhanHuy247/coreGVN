/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.backlist;

import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rua
 */
public class BannedWordManger {

    private static final Set<String> bannedWords = new HashSet<>();

    public static void init() {
        bannedWords.add("変態");
        bannedWords.add("乳首");
        bannedWords.add("援助");
        bannedWords.add("援交");
        bannedWords.add("ワリキリ");
        bannedWords.add("割り切り");
        bannedWords.add("エロ");
        bannedWords.add("エ□");
        bannedWords.add("エ口");
        bannedWords.add("欲求不満");
        bannedWords.add("イチャイチャ");
        bannedWords.add("いちゃいちゃ");
        bannedWords.add("パンティ");
        bannedWords.add("パンツ");
        bannedWords.add("モザ無し");
        bannedWords.add("むらむら");
        bannedWords.add("ムラムラ");
        bannedWords.add("見せ合い");
        bannedWords.add("見せっこ");
        bannedWords.add("オッパイ");
        bannedWords.add("おっぱい");
        bannedWords.add("エ○");
        bannedWords.add("えろ");
        bannedWords.add("おなに");
        bannedWords.add("おな写");
        bannedWords.add("オ○ニ");
        bannedWords.add("オナニー");
        bannedWords.add("オナ");
        bannedWords.add("Hな");
        bannedWords.add("H写");
        bannedWords.add("Hし");
        bannedWords.add("えっち");
        bannedWords.add("エッチ");
        bannedWords.add("ペニス");
        bannedWords.add("ちんぽ");
        bannedWords.add("チンコ");
        bannedWords.add("ちんこ");
        bannedWords.add("ちんちん");
        bannedWords.add("チンチン");
        bannedWords.add("セフレ");
        bannedWords.add("せっくす");
        bannedWords.add("せくーす");
        bannedWords.add("セックス");
        bannedWords.add("sex");
        bannedWords.add("SEX");
        bannedWords.add("おめこ");
        bannedWords.add("マンコ");
        bannedWords.add("まんこ");
        bannedWords.add("クリトリス");
        bannedWords.add("くりとりす");
//        bannedWords.add("クリ");
        bannedWords.add("売春");
        bannedWords.add("ローター");
        bannedWords.add("バイブ");
        bannedWords.add("対面座位");
        bannedWords.add("イメプ");
        bannedWords.add("イメージプレイ");
        bannedWords.add("放置プレイ");
        bannedWords.add("シコシコ");
        bannedWords.add("ドＭ");
        bannedWords.add("ドＳ");
        bannedWords.add("奴隷");
        bannedWords.add("虐めて");
        bannedWords.add("包茎");
        bannedWords.add("仮性包茎");
        bannedWords.add("淫語");
        bannedWords.add("淫乱");
        bannedWords.add("アソコ");
        bannedWords.add("お尻");
        bannedWords.add("全裸");
        bannedWords.add("勃起");
        bannedWords.add("射精");
        bannedWords.add("下着姿");
        bannedWords.add("巨根");
        bannedWords.add("貧乳");
        bannedWords.add("巨乳");
        bannedWords.add("美乳");
        bannedWords.add("乳");
        bannedWords.add("ペチャパイ");
        bannedWords.add("ぺちゃパイ");
        bannedWords.add("ぺちゃぱい");
        bannedWords.add("早漏");
        bannedWords.add("放尿");
        bannedWords.add("AV");
        bannedWords.add("アダルト");
        bannedWords.add("性生活");
        bannedWords.add("性感帯");
        bannedWords.add("舐め");
        bannedWords.add("フェラ");
        bannedWords.add("ふぇら");
        bannedWords.add("ＡＶ");
        bannedWords.add("ドS");
        bannedWords.add("ドM");
        bannedWords.add("ＳＥＸ");
        bannedWords.add("ｓｅｘ");
        bannedWords.add("チンポ");
        bannedWords.add("チンコ");
//        List<String> word;
//        try {
//            word = BannedWordDAO.getList(null);
//            for (int i = 0; i < word.size(); i++) {
//                bannedWords.add(word.get(i));
//            }
//        } catch (EazyException ex) {
//            Util.addErrorLog(ex);
//        }

    }

    public static Collection<String> toCollection() {
        return bannedWords;

    }
}
