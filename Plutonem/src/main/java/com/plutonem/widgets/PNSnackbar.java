package com.plutonem.widgets;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;
import com.plutonem.R;

import org.wordpress.android.util.AccessibilityUtils;

/**
 * {@link Snackbar} with custom colors and layout mimicking the updated design pattern defined in the Material Design
 * guidelines <a href="https://material.io/design/components/snackbars.html#spec">specifications</a>.  The views include
 * message and action button.  Any empty or null view is hidden.  The only required view is message.
 */
public class PNSnackbar {
    private Snackbar mSnackbar;
    private View mContentView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private PNSnackbar(@NonNull View view, @NonNull CharSequence text, int duration) {
        mSnackbar = Snackbar.make(view, "", // CHECKSTYLE IGNORE
                AccessibilityUtils.getSnackbarDuration(view.getContext(), duration));

        // Set underlying snackbar layout.
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) mSnackbar.getView();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarLayout.getLayoutParams();
        Context context = view.getContext();
        int margin = (int) context.getResources().getDimension(R.dimen.margin_medium);
        params.setMargins(margin, margin, margin, margin);
        snackbarLayout.setLayoutParams(params);
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.setBackground(context.getDrawable(R.drawable.bg_snackbar));

        // Hide underlying snackbar text and action.
        TextView snackbarText = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setVisibility(View.INVISIBLE);
        TextView snackbarAction = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_action);
        snackbarAction.setVisibility(View.INVISIBLE);

        mContentView = LayoutInflater.from(context).inflate(R.layout.snackbar, null);

        TextView message = mContentView.findViewById(R.id.message);

        // Hide message view when text is empty.
        if (TextUtils.isEmpty(text)) {
            message.setVisibility(View.GONE);
        } else {
            message.setVisibility(View.VISIBLE);
            message.setText(text);
        }

        snackbarLayout.addView(mContentView, 0);
    }

    public PNSnackbar addCallback(Snackbar.Callback callback) {
        mSnackbar.addCallback(callback);
        return this;
    }

    public void dismiss() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    public boolean isShowing() {
        return mSnackbar != null && mSnackbar.isShown();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static PNSnackbar make(@NonNull View view, @NonNull CharSequence text, int duration) {
        return new PNSnackbar(view, text, duration);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static PNSnackbar make(@NonNull View view, @StringRes int textRes, int duration) {
        CharSequence text = view.getResources().getString(textRes);
        return new PNSnackbar(view, text, duration);
    }

    private void setButtonTextAndVisibility(Button button, CharSequence text, final View.OnClickListener listener) {
        // Hide button when text is empty or listener is null.
        if (TextUtils.isEmpty(text) || listener == null) {
            button.setVisibility(View.GONE);
            button.setOnClickListener(null);
        } else {
            button.setVisibility(View.VISIBLE);
            button.setText(text);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    dismiss();
                }
            });
        }
    }

    public PNSnackbar setAction(CharSequence text, View.OnClickListener listener) {
        setButtonTextAndVisibility((Button) mContentView.findViewById(R.id.action), text, listener);
        return this;
    }

    public PNSnackbar setAction(@StringRes int textRes, View.OnClickListener listener) {
        CharSequence text = mContentView.getResources().getString(textRes);
        setButtonTextAndVisibility((Button) mContentView.findViewById(R.id.action), text, listener);
        return this;
    }

    public PNSnackbar setCallback(Snackbar.Callback callback) {
        mSnackbar.addCallback(callback);
        return this;
    }

    public void show() {
        mSnackbar.show();
    }
}

