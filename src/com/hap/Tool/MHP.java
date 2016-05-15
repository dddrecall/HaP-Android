package com.HaP.Tool;

import java.io.*;

public class MHP implements Serializable
{
	private String name;
	
	private String get;
	
	private String post;
	
	private String connection;
	

	public MHP(String name,String get,String post,String connection) throws Exception
	{
		this.name=name;
		this.get=get;
		this.post=post;
		this.connection=connection;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setGet(String get)
	{
		this.get = get;
	}

	public String getGet()
	{
		return get;
	}

	public void setPost(String post)
	{
		this.post = post;
	}

	public String getPost()
	{
		return post;
	}

	public void setConnection(String connection)
	{
		this.connection = connection;
	}

	public String getConnection()
	{
		return connection;
	}

	
	
}
