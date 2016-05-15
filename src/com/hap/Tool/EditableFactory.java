package com.HaP.Tool;


import android.text.*;
import android.util.*;

public class EditableFactory extends Editable.Factory
{
  private static Editable.Factory sInstance = new EditableFactory();

  /**
   * Returns the standard Editable Factory.
   */
  public static Editable.Factory getInstance() {
	return sInstance;
  }

  /**
   * Returns a new SpannedStringBuilder from the specified
   * CharSequence.  You can override this to provide
   * a different kind of Spanned.
   */
  public Editable newEditable(CharSequence source) {
	Log.e("edit","");
	if ( source instanceof SpannableStringBuilder ){
	  return (Editable)source;
	}
	return new SpannableStringBuilder(source);
  }

}


