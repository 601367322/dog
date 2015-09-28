package com.quanliren.quan_two.custom;/*package com.quanliren.quan_two.custom;


import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.custom.CustomDialog.Builder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
 
*//**
 *
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 *
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @author antoine vianey
 *
 *//*
public class ChoseFaceCustomDialog extends Dialog {
 
    public ChoseFaceCustomDialog(Context context, int theme) {
        super(context, theme);
    }
 
    public ChoseFaceCustomDialog(Context context) {
        super(context);
    }
 
    *//**
 * Helper class for creating a custom dialog
 *//*
    public static class Builder {
 
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private int gravity=Gravity.CENTER;
		private DialogInterface.OnClickListener 
                        positiveButtonClickListener,
                        negativeButtonClickListener;
        
        public Builder(Context context) {
            this.context = context;
        }
        *//**
 * Set the Dialog title from resource
 * @param title
 * @return
 *//*
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }
        *//**
 * Set the Dialog title from String
 * @param title
 * @return
 *//*
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public int getGravity() {
			return gravity;
		}
		public Builder setGravity(int gravity) {
			this.gravity = gravity;
			return this;
		}
 
 
        *//**
 * Set a custom content view for the Dialog.
 * If a message is set, the contentView is not
 * added to the Dialog...
 * @param v
 * @return
 *//*
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
 
        *//**
 * Set the positive button resource and it"s listener
 * @param positiveButtonText
 * @param listener
 * @return
 *//*
        public Builder setPositiveButton(int positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        *//**
 * Set the positive button text and it"s listener
 * @param positiveButtonText
 * @param listener
 * @return
 *//*
        public Builder setPositiveButton(String positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        *//**
 * Set the negative button resource and it"s listener
 * @param negativeButtonText
 * @param listener
 * @return
 *//*
        public Builder setNegativeButton(int negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        *//**
 * Set the negative button text and it"s listener
 * @param negativeButtonText
 * @param listener
 * @return
 *//*
        public Builder setNegativeButton(String negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
        ChoseFaceCustomDialog dialog;
        *//**
 * Create the custom dialog
 *//*
        public ChoseFaceCustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new ChoseFaceCustomDialog(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.customdialog_ios, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            if(title!=null&&!title.equals("")){
            	((TextView) layout.findViewById(R.id.title)).setVisibility(View.VISIBLE);
            	((TextView) layout.findViewById(R.id.title)).setText(title);
            }
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                	dialog.dismiss();
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
                ((Button) layout.findViewById(R.id.positiveButton))
                .setBackgroundResource(R.drawable.alert_all_dialog_button_bg);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(
                		R.id.message)).setText(message);
                ((TextView) layout.findViewById(
                		R.id.message)).setGravity(gravity);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, 
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT, 
                                        LayoutParams.WRAP_CONTENT));
            }else{
            	 ((LinearLayout) layout.findViewById(R.id.content)).setVisibility(View.GONE);
            }
            
            dialog.setContentView(layout);
            return dialog;
        }
 
    }
 
}*/