package com.HaP.View;


public class Item {
  private String title;
  private int resId;
  private String name;
  private String detail;

  public Item(String title,int resId, String name, String detail) {
		this.title=title;
		this.resId  = resId;
		this.name   = name;
		this.detail = detail;
  }

  public void setImageId(int resId) {
		this.resId  = resId;
  }
  public void setTitle(String title)
  {
		this.title=title;
  }
  public String getTitel()
  {
		return this.title;
  }
  public int getImageId() {
		return resId;
  }

  public void setName(String name) {
		this.name   = name;
  }

  public String getName() {
		return name;
  }

  public void setDetail(String detail) {
		this.detail = detail;
  }

  public String getDetail() {
		return detail;
  }

  public String toString() {
		return "Item[" + resId + ", " + name + ", " + detail + "]";
  }

}

