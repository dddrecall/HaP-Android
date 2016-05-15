/**
 *   920 Text Editor is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   920 Text Editor is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with 920 Text Editor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.HaP.Tool;

import android.text.*;
import java.io.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;

public class FileUtil
{
    /***
     * The number of bytes in a kilobyte.
     */
    public static final double ONE_KB = 1024.0;

    /***
     * The number of bytes in a megabyte.
     */
    public static final double ONE_MB = ONE_KB * ONE_KB;

    /***
     * The number of bytes in a gigabyte.
     */
    public static final double ONE_GB = ONE_KB * ONE_MB;

    public static final int DEFAULT_BUFFER_SIZE = 1024 * 8;
    public static final int EOF = -1;

    /***
     * Returns a human-readable version of the file size, where the input
     * represents a specific number of bytes.
     * 
     * @param size
     *            the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String byteCountToDisplaySize(long size)
    {
        return byteCountToDisplaySize((double) size);
    }

    public static String byteCountToDisplaySize(double size)
    {
        String displaySize;
        double ret;

        if ((ret = size / ONE_GB) > 1.0)
        {
            displaySize = " G";
        } else if ((ret = size / ONE_MB) > 1.0)
        {
            displaySize = " M";
        } else if ((ret = size / ONE_KB) > 1.0)
        {
            displaySize = " KB";
        } else
        {
            ret = size;
            displaySize = " B";
        }

        DecimalFormat df = new DecimalFormat("0.00");

        return df.format(ret) + displaySize;
    }

    /**
     * By default File#delete fails for non-empty directories, it works like
     * "rm". We need something a little more brutual - this does the equivalent
     * of "rm -r"
     * 
     * @param path
     *            Root File Path
     * @return true iff the file and all sub files/directories have been removed
     * @throws FileNotFoundException
     */
    public static boolean remove(File path)
    {
        if (path == null || !path.exists())
            return false;
        boolean ret = true;
        if (path.isDirectory())
        {
            for (File f : path.listFiles())
            {
                ret = ret && remove(f);
            }
        }
        return ret && path.delete();
    }

  
    /**
     * 读取整个文件, android默认编码为utf-8,如果文件编码是gbk或其它编码,要是没有指定正确的编码,就会统一当成ut-8编码处理
     * 
     * @param filename
     *            文件名
     * @param encoding
     *            指定文件编码,否则使用系统默认的编码
     * @return
     * @throws IOException 
     */
    public static String readFileAsString(String filename, String encoding) throws IOException
    {
        return readFileAsString(new File(filename), encoding);
    }

    public static String readFileAsString(File filename, String encoding) throws IOException
    {
        FileInputStream fis = new FileInputStream(filename);
        return readFileAsString(fis, encoding);
    }

    public static String readFileAsString(InputStream input, String encoding) throws IOException
    {
        StringBuilder output = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(input, Charset.forName(encoding));
        int n = 0;
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];

        while (EOF != (n = isr.read(buffer)))
            output.append(new String(buffer, 0, n));

        if (input != null)
            input.close();

        return output.toString();
    }
    
 
}
