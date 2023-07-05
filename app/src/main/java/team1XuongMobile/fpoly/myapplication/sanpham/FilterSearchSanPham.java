package team1XuongMobile.fpoly.myapplication.sanpham;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterSearchSanPham extends Filter {
    ArrayList<SanPhamModels> list;
    SanPhamAdapter adapter;

    public FilterSearchSanPham(ArrayList<SanPhamModels> list, SanPhamAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if (charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase().trim();
            ArrayList<SanPhamModels> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SanPhamModels item = list.get(i);
                String productName = item.getTen_loai().toUpperCase();
                String skuCode = item.getMaSp().toUpperCase();
                if (productName.contains(charSequence) || skuCode.contains(charSequence)) {
                    establishes.add(item);
                }
            }

            filterResults.count = establishes.size();
            filterResults.values = establishes;
        } else {
            filterResults.count = list.size();
            filterResults.values = list;
        }


        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.sanPhamModelsArrayList = (ArrayList<SanPhamModels>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}