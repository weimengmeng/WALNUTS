package com.njjd.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoUtil {
	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	// 根据路径，指定宽高获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmapWH(String filePath, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 把bitmap转换成String
	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	// 保存小图
	public static void saveMyBitmap(String from) {
		try {
			File file = new File(Environment.getExternalStorageDirectory()+
					"/walnuts/tempmini.jpg");
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			Bitmap resultbm = getSmallBitmap(from);
			int angle = readPictureDegree(from);
			Log.i("dgree", angle + "度");
			if (angle != 0)
				resultbm = rotaingImageView(angle * -1, resultbm);
			resultbm.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 将图片压缩到流中
			bos.flush();// 输出
			bos.close();// 关闭
			resultbm.recycle();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 指定宽高保存
	public static String saveMyBitmapWH(String from, int width, int height) {
		String path = "";
		try {
			path = Environment.getExternalStorageDirectory()
					+ "/walnuts/" + System.currentTimeMillis()
					+ ".jpg";
			File dirFirstFolder = new File(Environment.getExternalStorageDirectory()
					+ "/walnuts");//方法二：通过变量文件来获取需要创建的文件夹名字
			if(!dirFirstFolder.exists())
			{ //如果该文件夹不存在，则进行创建
				dirFirstFolder.mkdirs();//创建文件夹
			}
			File file = new File(path);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			Bitmap resultbm = getSmallBitmapWH(from, width, height);
			int angle = readPictureDegree(from);
			Log.i("dgree", angle + "度");
			if (angle != 0)
				resultbm = rotaingImageView(angle * -1, resultbm);
			if(resultbm!=null){
			resultbm.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 将图片压缩到流中
			bos.flush();// 输出
			bos.close();// 关闭
			resultbm.recycle();
			}else{
				bos.flush();// 输出
				bos.close();// 关闭
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
}
