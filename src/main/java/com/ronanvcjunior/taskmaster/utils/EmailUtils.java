package com.ronanvcjunior.taskmaster.utils;

public class EmailUtils {
    public static String getEmailMessage(String nome, String host, String chave) {
        return "Olá " +
                nome +
                ",\n\nSua nova conta foi criada." +
                " Por favor, clique no link abaixo para verificar sua conta.\n\n" +
                getVerificationUrl(host, chave) +
                "\n\nA Equipe de Suporte";
    }

    public static String getResetPasswordMessage(String nome, String host, String chave) {
        return "Olá " +
                nome +
                ",\n\nPor favor, clique no link abaixo para redefinir sua senha.\n\n" +
                getVerificationUrl(host, chave) +
                "\n\nA Equipe de Suporte";
    }

    private static String getVerificationUrl(String host, String chave) {
        return host + "/user/verify/account?key=" + chave;
    }

    private static String getResetPasswordUrl(String host, String chave) {
        return host + "/user/verify/password?key=" + chave;
    }
}
