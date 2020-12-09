package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Post;

public class PostValidator {
    public static List<String> validate(Post p) {
        List<String> errors = new ArrayList<String>();

        String title_error = _validateTitle(p.getTitle());
        if (!title_error.equals("")) {
            errors.add(title_error);
        }

        String content_error = _validateContent(p.getContent());
        if (!content_error.equals("")) {
            errors.add(content_error);
        }

        String image_error = _validateImage(p.getImage());
        if (!image_error.equals("")) {
            errors.add(image_error);
        }

        return errors;
    }

    // title必須入力チェック
    private static String _validateTitle(String title) {
        if (title == null || title.equals("")) {
            return "タイトルを入力してください。";
        }
        return "";
    }

    // content必須入力チェック
    private static String _validateContent(String content) {
        if (content == null || content.equals("")) {
            return "内容を入力してください";
        }
        return "";
    }

    // image必須入力チェック
    private static String _validateImage(String image) {
        if (image == null || image.equals("")) {
            return "画像を選択してください。";
        }
        return "";
    }

}
