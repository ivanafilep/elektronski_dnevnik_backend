//package com.ivana.tema8.util;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//

//public class Encryption {
//	public static String getEncodedPassword(String password) {
//		BCryptPasswordEncoder bce = new BCryptPasswordEncoder();
//		return bce.encode(password);
//	}
//	
//	
//	public static boolean validatePassword(String password, String encodedPassword) {
//		BCryptPasswordEncoder bce = new BCryptPasswordEncoder();
//		return bce.matches(password, encodedPassword);
//	}
//	
//
//
//  public static void main(String[] args) {
// System.out.println(getPassEncoded("pass"));
//}
//}