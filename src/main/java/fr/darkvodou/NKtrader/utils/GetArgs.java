package fr.darkvodou.NKtrader.utils;

public class GetArgs
{
	public static int getPlaceArg(String[] args, String argToFind)
	{
		int i = 0;

		for(String arg : args)
		{
			if(arg.equals(argToFind))
			{

				return i;
			}

			i++;
		}

		return -1;
	}
}
