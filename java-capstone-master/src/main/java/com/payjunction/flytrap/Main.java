package com.payjunction.flytrap;

import java.io.IOException;

class Main
{
	public static void main(String[] args) throws IOException
	{
		POS pos = new POS();

		runApplication(pos);
	}

	private static void runApplication(POS pos)
	{
		pos.menu();
	}
}
