package com.example.android.galleryapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.android.galleryapp.databinding.ChipColorBinding;
import com.example.android.galleryapp.databinding.ChipLabelBinding;
import com.example.android.galleryapp.databinding.DialogAddImageBinding;
import com.example.android.galleryapp.models.Item;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Set;

public class AddImageDialog implements ItemHelper.OnCompleteListener {
    private Context context;
    private OnCompleteListener listener;
    private DialogAddImageBinding b;
    private LayoutInflater inflater;
    private boolean isCustomLabel;
    private Bitmap image;
    private AlertDialog dialog;

    void show(Context context, OnCompleteListener listener){
        this.context = context;
        this.listener = listener;

        //Inflate dialog's layout
        if(context instanceof GalleryActivity){
            inflater = ((GalleryActivity) context).getLayoutInflater();
            b = DialogAddImageBinding.inflate(inflater);
        } else {
            dialog.dismiss();
            listener.onError("Cast Exception");
            return;
        }

        //Create and show dialog
        dialog = new MaterialAlertDialogBuilder(context,R.style.CustomDialogTheme)
                .setView(b.getRoot())
                .show();

        //Hide errors from ET(Utils)
        hideErrorFromET();

        //Handle events(Step 1: Input Dimensions)
        handleDimensionsInput();

    }

    private void hideErrorFromET() {
        b.inputWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b.inputWidth.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void handleDimensionsInput() {
        b.fetchImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get strings from ET
                String widthStr = b.inputWidth.getText().toString().trim()
                        ,heightStr = b.inputHeight.getText().toString().trim();

                //Guard code
                if(widthStr.isEmpty() && heightStr.isEmpty()){
                    b.inputWidth.setError("Please enter atleast one dimension!");
                    return;
                }

                //UPDATE UI
                b.enterDimenRoot.setVisibility(View.GONE);
                b.linearProgressIndicatorRoot.setVisibility(View.VISIBLE);
                hideKeyboard();

                //For square iamge
                if(widthStr.isEmpty()){
                    int height = Integer.parseInt(heightStr);
                    fetchRandomImage(height);
                }
                else if(heightStr.isEmpty()){
                    int width = Integer.parseInt(widthStr);
                    fetchRandomImage(width);
                }

                //For rectangular iamge
                else{
                    int height = Integer.parseInt(heightStr);
                    int width = Integer.parseInt(widthStr);
                    fetchRandomImage(width,height);
                }
            }
        });
    }

    private void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(b.inputWidth.getWindowToken(), 0);
    }



    //Step 2: Fetch image

    //For rectangular image
    private void fetchRandomImage(int width, int height) {
        new ItemHelper()
                .fetchData(width, height, context, this);
    }


    //For square image
    private void fetchRandomImage(int x) {
        new ItemHelper()
                .fetchData(x, context, this);
    }



    //Step 3: Show data
    private void showData(Bitmap image, Set<Integer> colors, List<String> labels) {
        b.imageView.setImageBitmap(image);
        inflateColorChips(colors);
        inflateLabelChips(labels);

        b.linearProgressIndicatorRoot.setVisibility(View.GONE);
        b.mainRoot.setVisibility(View.VISIBLE);
        b.customInputEt.setVisibility(View.GONE);
        handleCustomLabelInput();
        handleAddImageEvent();
    }

    private void handleAddImageEvent() {
        b.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorChipId = b.colorChips.getCheckedChipId()
                        , labelChipId = b.labelChips.getCheckedChipId();

                //Guard Code
                if(colorChipId==-1 || labelChipId==-1){
                    Toast.makeText(context, "Please choose color & label", Toast.LENGTH_SHORT).show();
                    return;
                }

                String label;

                //Get color & label
                if(isCustomLabel){
                    label = b.customInputEt.getText().toString().trim();
                    if(label.isEmpty()){
                        Toast.makeText(context, "Please enter custom label", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    label = ((Chip) b.labelChips.findViewById(labelChipId))
                            .getText().toString();
                }

                int color = ((Chip) b.colorChips.findViewById(colorChipId))
                        .getChipBackgroundColor().getDefaultColor();


                //Send callbacks
                listener.onImageAdded(new Item(image,color,label));
                dialog.dismiss();
            }
        });
    }

    //Handles Custom label input
    private void handleCustomLabelInput() {
        ChipLabelBinding binding = ChipLabelBinding.inflate(inflater);
        binding.getRoot().setText("Custom");
        b.labelChips.addView(binding.getRoot());

        binding.getRoot().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    b.customInputEt.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    isCustomLabel = isChecked;
            }
        });
    }

    //Label Chips
    private void inflateLabelChips(List<String> labels) {
        for (String label : labels) {
            ChipLabelBinding binding = ChipLabelBinding.inflate(inflater);
            binding.getRoot().setText(label);
            b.labelChips.addView(binding.getRoot());
        }
    }

    //Color Chips
    private void inflateColorChips(Set<Integer> colors) {
        for (int color : colors) {
            ChipColorBinding binding = ChipColorBinding.inflate(inflater);
            binding.getRoot().setChipBackgroundColor(ColorStateList.valueOf(color));
            b.colorChips.addView(binding.getRoot());
        }
    }



    //ItemHelper callbacks
    @Override
    public void onFetched(Bitmap image, Set<Integer> colors, List<String> labels) {
        this.image = image;
        showData(image,colors,labels);
    }

    @Override
    public void onError(String error) {
        dialog.dismiss();
        listener.onError(error);
    }


    interface OnCompleteListener{
        void onImageAdded(Item item);
        void onError(String error);
    }
}
