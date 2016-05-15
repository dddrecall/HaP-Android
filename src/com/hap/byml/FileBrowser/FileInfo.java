package com.HaP.Byml.FileBrowser;
import com.HaP.Byml.*;

/** 表示一个文件实体 **/
public class FileInfo {
	public String Name;
	public String Path;
	public long Size;
	public boolean IsDirectory = false;
	public int FileCount = 0;
	public int FolderCount = 0;

	public int getIconResourceId() {
		if (IsDirectory) {
			return R.drawable.folder;
		}
		return R.drawable.doc;
	}
}
