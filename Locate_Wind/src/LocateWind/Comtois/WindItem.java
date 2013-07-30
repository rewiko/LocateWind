package LocateWind.Comtois;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

public class WindItem extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> arrayListOverlayItem = new ArrayList();
	private Context context;

	public WindItem(Drawable paramDrawable) {
		super(boundCenterBottom(paramDrawable));
	}

	public WindItem(Drawable paramDrawable, Context paramContext) {
		super(boundCenterBottom(paramDrawable));
		this.context = paramContext;
	}

	public void addOverlayItem(OverlayItem paramOverlayItem) {
		try {
			this.arrayListOverlayItem.add(paramOverlayItem);
			populate();

		} catch (Exception e) {
		}
	}

	@Override
	protected OverlayItem createItem(int paramInt) {
		return (OverlayItem) this.arrayListOverlayItem.get(paramInt);
	}

	@Override
	protected boolean onTap(int index) {

		try {
			OverlayItem localOverlayItem = (OverlayItem) this.arrayListOverlayItem
					.get(index);
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(
					this.context);
			localBuilder.setTitle(localOverlayItem.getTitle());
			localBuilder.setMessage(localOverlayItem.getSnippet());
			localBuilder.show();

		} catch (Exception e) {

		}
		return true;

	}

	@Override
	public int size() {
		return this.arrayListOverlayItem.size();
	}
}