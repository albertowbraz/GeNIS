package br.genis.servicos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServicoMD5 {

	private static MessageDigest md;

	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
	}

	private static char[] hexCodes(byte[] text) {
		char[] hexOutput = new char[text.length * 2];
		String hexString;

		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.toUpperCase().getChars(hexString.length() - 2,
					hexString.length(), hexOutput, i * 2);
		}
		return hexOutput;
	}

	public static String criptografar(String senha) {
		String senhaCript = null;

		if (md != null) {
			senhaCript = new String(hexCodes(md.digest(senha.getBytes())));
		}

		return senhaCript;
	}

}
