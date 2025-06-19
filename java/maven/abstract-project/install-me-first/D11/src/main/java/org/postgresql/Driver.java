package org.postgresql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Driver {
	public Driver() throws IOException {
		System.out.println("88                                88                           88  \n" +
				"88                                88                           88  \n" +
				"88                                88                           88  \n" +
				"88,dPPYba,  ,adPPYYba,  ,adPPYba, 88   ,d8  ,adPPYba,  ,adPPYb,88  \n" +
				"88P'    \"8a \"\"     `Y8 a8\"     \"\" 88 ,a8\"  a8P_____88 a8\"    `Y88  \n" +
				"88       88 ,adPPPPP88 8b         8888[    8PP\"\"\"\"\"\"\" 8b       88  \n" +
				"88       88 88,    ,88 \"8a,   ,aa 88`\"Yba, \"8b,   ,aa \"8a,   ,d88  \n" +
				"88       88 `\"8bbdP\"Y8  `\"Ybbd8\"' 88   `Y8a `\"Ybbd8\"'  `\"8bbdP\"Y8  ");
	}

	public Connection connect(String url, Properties info) throws SQLException {
		return null;
	}
}
