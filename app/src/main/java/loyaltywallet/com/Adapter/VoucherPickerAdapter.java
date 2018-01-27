package loyaltywallet.com.Adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import loyaltywallet.com.Interfaces.VoucherPicker;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.VoucherPickerItemBinding;

/**
 * Created by hemantsingh on 18/06/17.
 */

public class VoucherPickerAdapter extends RecyclerView.Adapter<VoucherPickerAdapter.ViewHolder> {

    private final List<Voucher> mValues;
    private final VoucherPicker mListener;

    public VoucherPickerAdapter(List<Voucher> items, VoucherPicker listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public VoucherPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        VoucherPickerItemBinding itemBinding =
                VoucherPickerItemBinding.inflate(layoutInflater, parent, false);
        return new VoucherPickerAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final VoucherPickerAdapter.ViewHolder holder, int position) {
        holder.bind(mValues.get(position),position);
        Voucher voucher = mValues.get(position);
        holder.itemBinding.getRoot().setAlpha(voucher.getStatus().equals("Redeemed") ? (float) 0.5 : (float) 1.0);
        if(voucher.isSelected) {
            holder.itemBinding.getRoot().setBackground(ContextCompat.getDrawable(holder.itemBinding.getRoot().getContext(), R.drawable.palewood));
        }
        else {
            holder.itemBinding.getRoot().setBackground(ContextCompat.getDrawable(holder.itemBinding.getRoot().getContext(), R.drawable.background));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final VoucherPickerItemBinding itemBinding;
        public ViewHolder(VoucherPickerItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Voucher item, int position) {
            itemBinding.setData(item);
            itemBinding.setMlistener(mListener);
            itemBinding.setIndex(position);
            itemBinding.executePendingBindings();
        }
    }

}
