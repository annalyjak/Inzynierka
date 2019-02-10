package lyjak.anna.inzynierka.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.model.reports.Currency;

/**
 * Created by Anna on 22.10.2017.
 */

public class CurrencyAdapter extends BaseAdapter {
    Context context;
    Currency[] values;
    int[] bitmaps = {R.drawable.all,
            R.drawable.azn,
            R.drawable.bam,
            R.drawable.bgn,
            R.drawable.byn,
            R.drawable.chf,
            R.drawable.czk,
            R.drawable.dkk,
            R.drawable.eur,
            R.drawable.uk,
            R.drawable.gel,
            R.drawable.hrk,
            R.drawable.huf,
            R.drawable.isk,
            R.drawable.mdl,
            R.drawable.mdk,
            R.drawable.nok,
            R.drawable.pln,
            R.drawable.ron,
            R.drawable.rsd,
            R.drawable.rub,
            R.drawable.sek,
            R.drawable.tr,
            R.drawable.uah
    };
    LayoutInflater inflter;

    public CurrencyAdapter(Context applicationContext) {
        this.context = applicationContext;
        this.values = Currency.values();
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return values[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_currency, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(bitmaps[i]);
        names.setText(values[i].toString());
        return view;
    }
}
