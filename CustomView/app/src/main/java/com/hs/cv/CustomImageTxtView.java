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
 * ͼ�ļ��ؿؼ�
 * 		ʹ�÷����������dimen�е�һ�ؼ��Ŀ�ߣ�������xml��ͳһʹ��
 * 		һЩ�������ã��������ö�����������
 *
 * @author Holy-Spirit
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class CustomImageTxtView extends LinearLayout {

    /*
     * �ؼ���EditText����
     */
    private EditText mText = null;
    /*
     * �ؼ���ImageView����
     */
    private ImageView mImage = null;

    public CustomImageTxtView(Context context) {
        super(context);
    }

    public CustomImageTxtView(Context context, AttributeSet attrs) {
        super(context, attrs);

		/*
		 * �ؼ��Ŀ�
		 */
        final int vWidth = context.getResources().getDimensionPixelSize(
                R.dimen.defWidth);
		/*
		 * �ؼ��ĸ�
		 */
        final int vHeight = context.getResources().getDimensionPixelSize(
                R.dimen.defHeight);
		/*
		 * �ؼ���imag�ĸ�
		 */
        final int imgHeight = vHeight * 3 / 10;
		/*
		 * �ؼ���imag�Ŀ�
		 */
        final int imgWidth = imgHeight;
		/*
		 * �ؼ���imag���������ؼ��ļ��
		 */
        final int imgMargin = vHeight / 5;

		/*
		 * �ؼ����ݷ��õ�����
		 */
        final int contentGra = Gravity.CENTER_VERTICAL;

		/*
		 * �ؼ���txt�ı��Ŀ��
		 */
        final int txtWidth = vWidth - 2 * imgMargin - imgWidth;

		/*
		 * �ؼ���txt�ı��ĸ߶�
		 */
        final int txtHeight = vHeight;

		/*
		 * ���Ե�id
		 */
        int resId = -1;

		/*��������View������*/
        this.setGravity(contentGra);

		/*��ȡ�Զ������������*/
        TypedArray mArray = context.obtainStyledAttributes(attrs,
                R.styleable.CustomImageTxtView);

		/*����Image*/
        mImage = new ImageView(context);
        this.setImageLayoutParams(imgWidth, imgHeight, imgMargin);

		/*����Edit*/
        mText = new EditText(context);
        this.setTextGravity(contentGra);
        this.setTextLayoutParams(txtWidth, txtHeight);
        this.hideBottomLine(context.getResources().getDrawable(
                R.drawable.round_background));
        this.setIsSingleLine(true);


		/*��ȡxml�����õ�����*/
        int length = mArray.getIndexCount();
        for (int i = 0; i < length; i++) {

            int attr = mArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageTxtView_Oriental:
				/*���÷���*/
                    resId = mArray.getInt(R.styleable.CustomImageTxtView_Oriental, 0);
                    this.setOrientation((resId == 1) ? LinearLayout.HORIZONTAL
                            : LinearLayout.VERTICAL);
                    break;

                case R.styleable.CustomImageTxtView_Text:
				/*�����ı�����*/
                    resId = mArray.getResourceId(R.styleable.CustomImageTxtView_Text, 0);
                    mText.setText(resId > 0 ? mArray.getResources().getText(resId)
                            : mArray.getString(R.styleable.CustomImageTxtView_Text));
                    break;

                case R.styleable.CustomImageTxtView_Src:
				/*����ͼƬ��Դ*/
                    resId = mArray.getResourceId(R.styleable.CustomImageTxtView_Src, 0);

                    mImage.setImageBitmap(resId > 0 ? BitmapUtil
                            .decodeSampleBitmapFromResource(getResources(), resId,
                                    imgWidth, imgHeight) : BitmapUtil
                            .decodeSampleBitmapFromResource(getResources(),
                                    R.drawable.ic_launcher, imgWidth, imgHeight));
                    break;

                case R.styleable.CustomImageTxtView_Content:
				/*�����ı�����*/
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
				/*����������ɫ*/
                    resId = mArray.getResourceId(
                            R.styleable.CustomImageTxtView_TextColor, 0);
                    if (resId > 0)
                        this.setTextColor(context.getResources().getColor(resId));
                    break;

                case R.styleable.CustomImageTxtView_TextSize:
				/*�������ִ�С*/
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

		/*��ӿؼ������ؼ�*/
        addView(mImage);
        addView(mText);


		/*������Դ*/
        mArray.recycle();

    }


    /*
     * ������������
     */
    public void setTextTransformationMethod() {
        mText.setTransformationMethod(PasswordTransformationMethod
                .getInstance());

    }



    /*
     * ������������
     */
    public void setTextInputType(int type) {
        mText.setInputType(type);
    }



    /*
     * �������������С
     */
    public void setTextSize(int size) {
        mText.setTextSize(size);

    }



    /*
     * ��������������ɫ
     */
    public void setTextColor(int color) {
        mText.setTextColor(color);
    }


    /*
     * ����Imag����
     */
    public void setImageLayoutParams(int reqWidth, int reqHeight, int margin) {
        LinearLayout.LayoutParams mParams = new LayoutParams(reqWidth,
                reqHeight);
        mParams.setMargins(margin, margin, margin, margin);
        mImage.setLayoutParams(mParams);
    }


    /*
     * �����ı�����
     */
    public void setIsSingleLine(boolean isSingleLine) {
        mText.setSingleLine(isSingleLine);
    }


    /*
     * ����ԭ��EditText���»���
     */
    public void hideBottomLine(Drawable bg) {
        mText.setBackground(bg);
    }


    /*
     * �����ı���������
     */
    public void setTextGravity(int gravity) {
        mText.setGravity(gravity);
    }


    /*
     * �����ı�����
     */
    public void setTextLayoutParams(int txtWidth, int txtHeight) {

        mText.setLayoutParams(new LayoutParams(txtWidth, txtHeight));

    }
}