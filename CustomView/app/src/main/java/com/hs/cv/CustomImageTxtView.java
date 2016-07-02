package com.hs.cv;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hs.customview.R;
import com.hs.cv.utils.BitmapUtil;

/**
 * 图文加载控件
 * 		使用方法，最好在dimen中第一控件的宽高，并且在xml中统一使用
 * 		一些属性设置，可以引用对象，重新设置
 *
 * @author Holy-Spirit
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class CustomImageTxtView extends LinearLayout {

    /*
     * 控件内EditText对象
     */
    private EditText mText = null;
    /*
     * 控件内ImageView对象
     */
    private ImageView mImage = null;

    public CustomImageTxtView(Context context) {
        super(context);
    }

    public CustomImageTxtView(Context context, AttributeSet attrs) {
        super(context, attrs);

		/*
		 * 控件的宽
		 */
        final int vWidth = context.getResources().getDimensionPixelSize(
                R.dimen.defWidth);
		/*
		 * 控件的高
		 */
        final int vHeight = context.getResources().getDimensionPixelSize(
                R.dimen.defHeight);
		/*
		 * 控件内imag的高
		 */
        final int imgHeight = vHeight * 3 / 10;
		/*
		 * 控件内imag的宽
		 */
        final int imgWidth = imgHeight;
		/*
		 * 控件内imag的与其他控件的间隔
		 */
        final int imgMargin = vHeight / 5;

		/*
		 * 控件内容放置的重心
		 */
        final int contentGra = Gravity.CENTER_VERTICAL;

		/*
		 * 控件内txt文本的宽度
		 */
        final int txtWidth = vWidth - 2 * imgMargin - imgWidth;

		/*
		 * 控件内txt文本的高度
		 */
        final int txtHeight = vHeight;

		/*
		 * 属性的id
		 */
        int resId = -1;

		/*设置整个View的重心*/
        this.setGravity(contentGra);

		/*获取自定义的属性数组*/
        TypedArray mArray = context.obtainStyledAttributes(attrs,
                R.styleable.CustomImageTxtView);

		/*设置Image*/
        mImage = new ImageView(context);
        this.setImageLayoutParams(imgWidth, imgHeight, imgMargin);

		/*设置Edit*/
        mText = new EditText(context);
        this.setTextGravity(contentGra);
        this.setTextLayoutParams(txtWidth, txtHeight);
        this.hideBottomLine(context.getResources().getDrawable(
                R.drawable.round_background));
        this.setIsSingleLine(true);


		/*获取xml中设置的属性*/
        int length = mArray.getIndexCount();
        for (int i = 0; i < length; i++) {

            int attr = mArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageTxtView_Oriental:
				/*设置方向*/
                    resId = mArray.getInt(R.styleable.CustomImageTxtView_Oriental, 0);
                    this.setOrientation((resId == 1) ? LinearLayout.HORIZONTAL
                            : LinearLayout.VERTICAL);
                    break;

                case R.styleable.CustomImageTxtView_Text:
				/*设置文本内容*/
                    resId = mArray.getResourceId(R.styleable.CustomImageTxtView_Text, 0);
                    mText.setText(resId > 0 ? mArray.getResources().getText(resId)
                            : mArray.getString(R.styleable.CustomImageTxtView_Text));
                    break;

                case R.styleable.CustomImageTxtView_Src:
				/*设置图片资源*/
                    resId = mArray.getResourceId(R.styleable.CustomImageTxtView_Src, 0);

                    mImage.setImageBitmap(resId > 0 ? BitmapUtil
                            .decodeSampleBitmapFromResource(getResources(), resId,
                                    imgWidth, imgHeight) : BitmapUtil
                            .decodeSampleBitmapFromResource(getResources(),
                                    R.drawable.ic_launcher, imgWidth, imgHeight));
                    break;

                case R.styleable.CustomImageTxtView_Content:
				/*设置文本类型*/
                    resId = mArray.getInt(R.styleable.CustomImageTxtView_Content, 1);
                    if (resId == 1) {
                        this.setTextInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    } else if (resId == 2) {
                        this.setTextInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        this.setTextTransformationMethod();
                    } else {
                        this.setTextInputType(InputType.TYPE_CLASS_TEXT);
                    }
                    break;
                case R.styleable.CustomImageTxtView_TextColor:
				/*设置文字颜色*/
                    resId = mArray.getResourceId(
                            R.styleable.CustomImageTxtView_TextColor, 0);
                    if (resId > 0)
                        this.setTextColor(context.getResources().getColor(resId));
                    break;

                case R.styleable.CustomImageTxtView_TextSize:
				/*设置文字大小*/
                    resId = mArray.getResourceId(
                            R.styleable.CustomImageTxtView_TextSize, 0);
                    if (resId > 0)
                        this.setTextSize(context.getResources()
                                .getDimensionPixelSize(resId));
                    break;
                default:
                    break;
            }
        }

		/*添加控件到父控件*/
        addView(mImage);
        addView(mText);


		/*回收资源*/
        mArray.recycle();

    }


    /*
     * 设置密码输入
     */
    public void setTextTransformationMethod() {
        mText.setTransformationMethod(PasswordTransformationMethod
                .getInstance());

    }



    /*
     * 设置内容类型
     */
    public void setTextInputType(int type) {
        mText.setInputType(type);
    }



    /*
     * 设置内容字体大小
     */
    public void setTextSize(int size) {
        mText.setTextSize(size);

    }



    /*
     * 设置内容字体颜色
     */
    public void setTextColor(int color) {
        mText.setTextColor(color);
    }


    /*
     * 设置Imag布局
     */
    public void setImageLayoutParams(int reqWidth, int reqHeight, int margin) {
        LinearLayout.LayoutParams mParams = new LayoutParams(reqWidth,
                reqHeight);
        mParams.setMargins(margin, margin, margin, margin);
        mImage.setLayoutParams(mParams);
    }


    /*
     * 设置文本单行
     */
    public void setIsSingleLine(boolean isSingleLine) {
        mText.setSingleLine(isSingleLine);
    }


    /*
     * 隐藏原本EditText的下划线
     */
    public void hideBottomLine(Drawable bg) {
        mText.setBackground(bg);
    }


    /*
     * 设置文本内容重心
     */
    public void setTextGravity(int gravity) {
        mText.setGravity(gravity);
    }


    /*
     * 设置文本布局
     */
    public void setTextLayoutParams(int txtWidth, int txtHeight) {

        mText.setLayoutParams(new LayoutParams(txtWidth, txtHeight));

    }
}