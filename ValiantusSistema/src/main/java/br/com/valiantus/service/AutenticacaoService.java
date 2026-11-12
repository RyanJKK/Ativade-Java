package br.com.valiantus.service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
public class AutenticacaoService {
    private static final int ITERACOES = 120000;
    public static String gerarHash(char[] senha) throws Exception {
        byte[] sal = new byte[16]; new SecureRandom().nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal) + ":" + derivar(senha, sal);
    }
    private static String derivar(char[] senha, byte[] sal) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(senha, sal, ITERACOES, 256);
        try { return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded()); }
        finally { spec.clearPassword(); }
    }
    public static boolean verificar(char[] senha, String armazenado) throws Exception {
        String[] partes = armazenado.split(":");
        if (partes.length != 2) return false;
        byte[] esperado = Base64.getDecoder().decode(partes[1]);
        byte[] recebido = Base64.getDecoder().decode(derivar(senha, Base64.getDecoder().decode(partes[0])));
        return java.security.MessageDigest.isEqual(esperado, recebido);
    }
}
