package com.skillengine.main;

public class SkillEngineMain
{

	public static void main( String[] args )
	{
		SkillEngineImpl engineImpl = SkillEngineImpl.init();
		engineImpl.initMessageQueue();

	}

}
