package com.forrestguice.suntimes.mappack;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews(this);
    }

    public void initViews(Context context)
    {
        TextView nameView = (TextView) findViewById(R.id.txt_about_name);
        /*nameView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openLink(v.getContext(), getString(R.string.help_app_url));
            }
        });*/

        TextView versionView = (TextView) findViewById(R.id.txt_about_version);
        versionView.setMovementMethod(LinkMovementMethod.getInstance());
        versionView.setText(fromHtml(htmlVersionString()));

        TextView supportView = (TextView) findViewById(R.id.txt_about_support);
        supportView.setMovementMethod(LinkMovementMethod.getInstance());
        supportView.setText(fromHtml(context.getString(R.string.about_app_support_field, context.getString(R.string.help_support_url))));

        TextView legalView1 = (TextView) findViewById(R.id.txt_about_legal1);
        legalView1.setMovementMethod(LinkMovementMethod.getInstance());
        legalView1.setText(fromHtml(context.getString(R.string.about_app_sourcecode_field)));

        TextView aboutMediaView = (TextView) findViewById(R.id.txt_about_media);
        aboutMediaView.setMovementMethod(LinkMovementMethod.getInstance());
        aboutMediaView.setText(fromHtml(initMediaCredits(context)));

        int[] linkViews = new int[] { R.id.txt_help_url, R.id.txt_about_url };
        for (int resID : linkViews)
        {
            TextView text = (TextView) findViewById(resID);
            if (text != null) {
                text.setText(fromHtml(anchor(text.getText().toString())));
                text.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        TextView helpView = (TextView) findViewById(R.id.txt_help_general);
        if (helpView != null) {
            helpView.setText(fromHtml(helpView.getText().toString()));
        }

        TextView manifestView = (TextView) findViewById(R.id.txt_about_manifest);
        if (manifestView != null)
        {
            StringBuilder manifest = new StringBuilder();
            String[] files = getResources().getStringArray(R.array.background_file);
            for (int i=0; i<files.length; i++) {
                manifest.append(files[i]).append("\n");
            }
            manifestView.setText(manifest);
        }
    }

    public static String initCredits(Context context, int stringResId, int entryArrayResId, int entryFormatResId)
    {
        final String[] entries = context.getResources().getStringArray(entryArrayResId);
        StringBuilder credits = new StringBuilder();
        for (int i=0; i<entries.length; i++)
        {
            credits.append(context.getString(entryFormatResId, entries[i]));
            if (i != entries.length-1) {
                credits.append(" <br />");
            }
        }
        return context.getString(stringResId, credits.toString());
    }

    public static String initMediaCredits(Context context) {
        return initCredits(context, R.string.about_media_field, R.array.about_media, R.string.about_libraryCreditsFormat);
    }

    public static void openLink(@Nullable Context context, @Nullable String url)
    {
        if (context == null || url == null) {
            return;
        }
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            Log.e("About", "openLink: " + e);
        }
    }

    public String htmlVersionString()
    {
        String buildString = anchor(getString(R.string.help_commit_url) + BuildConfig.GIT_HASH, BuildConfig.GIT_HASH);
        String versionString = anchor(getString(R.string.help_changelog_url), BuildConfig.VERSION_NAME) + " " + smallText("(" + buildString + ")");
        if (BuildConfig.DEBUG)
        {
            versionString += " " + smallText("[" + BuildConfig.BUILD_TYPE + "]");
        }
        return getString(R.string.about_app_version_field, versionString);
    }

    public static String anchor(String url) {
        return anchor(url, url);
    }
    public static String anchor(String url, String text) {
        return "<a href=\"" + url + "\">" + text + "</a>";
    }

    protected static String smallText(String text) {
        return "<small>" + text + "</small>";
    }

    public static Spanned fromHtml(String htmlString ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        else return Html.fromHtml(htmlString);
    }

}