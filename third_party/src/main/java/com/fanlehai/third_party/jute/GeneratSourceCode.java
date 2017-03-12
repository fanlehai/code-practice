package com.fanlehai.third_party.jute;

import org.apache.jute.compiler.generated.Rcc;

public class GeneratSourceCode {

	public static void main(String[] arg) {
		String params[] = new String[3];
        params[0] = "-l";
        params[1] = "java";
        params[2] = "src/main/resources/zookeeper.jute";
        Rcc.main(params);
	}
}
