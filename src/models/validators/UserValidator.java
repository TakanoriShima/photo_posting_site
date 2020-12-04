package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.User;
import utils.DBUtil;

public class UserValidator {
    public static List<String> validate(User u, Boolean mail_address_duplicate_check_flag,
            Boolean password_check_flag) {
        List<String> errors = new ArrayList<String>();

        String mail_address_error = _validateMail_address(u.getMail_address(), mail_address_duplicate_check_flag);
        if (!mail_address_error.equals("")) {
            errors.add(mail_address_error);
        }

        String name_error = _validateName(u.getName());
        if (!name_error.equals("")) {
            errors.add(name_error);
        }

        String password_error = _validatePassword(u.getPassword(), password_check_flag);
        if (!password_error.equals("")) {
            errors.add(password_error);
        }

        String icon_error = _validateIcon(u.getIcon());
        if (!icon_error.equals("")) {
            errors.add(icon_error);
        }

        return errors;

    }

    // メールアドレス
    private static String _validateMail_address(String mail_address, Boolean mail_address_duplicate_check_flag) {
        // 必須入力チェック
        if (mail_address == null || mail_address.equals("")) {
            return "メールアドレスを入力してください。";
        }
        // すでに登録されているメールアドレスとの重複チェック
        if (mail_address_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long user_count = (long) em.createNamedQuery("checkRegisteredMailAddress", Long.class)
                    .setParameter("mail_address", mail_address).getSingleResult();

            em.close();
            if (user_count > 0) {
                return "入力されたアドレスはすでに登録されています。";
            }
        }
        return "";
    }

    // 社員名の必須入力チェック
    private static String _validateName(String name) {
        if (name == null || name.equals("")) {
            return "氏名を入力してください。";
        }
        return "";
    }

    // パスワードの必須入力チェック
    private static String _validatePassword(String password, Boolean password_check_flag) {
        if (password == null || password.equals("")) {
            return "パスワードを入力してください";
        }
        return "";
    }

    // アイコンの入力チェック
    private static String _validateIcon(String icon){
        if(icon == null || icon.equals("")){
            return "アイコン画像を選択してください。";
        }
        return "";
    }

}
