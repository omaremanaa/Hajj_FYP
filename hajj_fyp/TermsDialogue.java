package com.example.hajj_fyp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class TermsDialogue extends AppCompatDialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Terms & Conditions").setMessage("Pilgrim's Mate is licensed to You (End-User) by Pligrim's Mate, located at King Saud University, Riyadh, Riyadh 12434, Saudi Arabia (hereinafter: Licensor), for use only under the terms of this License Agreement. \n" +
                "By downloading the Application from the Apple AppStore, and any update thereto (as permitted by this License Agreement), You indicate that You agree to be bound by all of the terms and conditions of this License Agreement, and that You accept this License Agreement. \n" +
                "The parties of this License Agreement acknowledge that Apple is not a Party to this License Agreement and is not bound by any provisions or obligations with regard to the Application, such as warranty, liability, maintenance and support thereof. Pligrim's Mate, not Apple, is solely responsible for the licensed Application and the content thereof. \n" +
                "This License Agreement may not provide for usage rules for the Application that are in conflict with the latest App Store Terms of Service. Pligrim's Mate acknowledges that it had the opportunity to review said terms and this License Agreement is not conflicting with them. \n" +
                "All rights not expressly granted to You are reserved.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
