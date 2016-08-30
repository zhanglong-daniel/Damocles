package com.damocles.sample;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewChoiceModeActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_choicemode);
        Utils.initToolbar(this, R.id.listview_choicemode_toolbar);
        initViews();
    }

    public void onPickCountryClick(View v) {
        int pos = mListView.getCheckedItemPosition();
        String msg = "Chosen country : " + (ListView.INVALID_POSITION == pos ? "null" : COUNTRIES[pos]);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.listview_choicemode_list);
        mListView.setAdapter(new CountryAdapter());
    }

    public class CountryAdapter extends BaseAdapter {

        public CountryAdapter() {
        }

        @Override
        public String getItem(int position) {
            return COUNTRIES[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return COUNTRIES.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CountryView(ListViewChoiceModeActivity.this);
            }
            String country = getItem(position);
            CountryView countryView = (CountryView) convertView;
            countryView.setTitle(country);
            return convertView;
        }
    }

    private class CountryView extends LinearLayout implements Checkable {

        private TextView mTitle;
        private CheckBox mCheckBox;

        public CountryView(Context context) {
            this(context, null);
        }

        public CountryView(Context context, AttributeSet attrs) {
            super(context, attrs);
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.activity_listview_choicemode_item, this, true);
            mTitle = (TextView) v.findViewById(R.id.listview_choicemode_item_title);
            mCheckBox = (CheckBox) v.findViewById(R.id.listview_choicemode_item_checkbox);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }

        @Override
        public boolean isChecked() {
            return mCheckBox.isChecked();
        }

        @Override
        public void setChecked(boolean checked) {
            mCheckBox.setChecked(checked);
        }

        @Override
        public void toggle() {
            mCheckBox.toggle();
        }

    }

    private static final String[] COUNTRIES = new String[] {
            "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
            "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda",
            "Argentina", "Armenia", "Aruba", "Australia", "Austria",
            "Azerbaijan", "Bahrain", "Bangladesh", "Barbados", "Belarus",
            "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
            "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil",
            "British Indian Ocean Territory", "British Virgin Islands",
            "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
            "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
            "Central African Republic", "Chad", "Chile", "China",
            "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
            "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
            "Cuba", "Cyprus", "Czech Republic",
            "Democratic Republic of the Congo", "Denmark", "Djibouti",
            "Dominica", "Dominican Republic", "East Timor", "Ecuador",
            "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
            "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands",
            "Fiji", "Finland", "Former Yugoslav Republic of Macedonia",
            "France", "French Guiana", "French Polynesia",
            "French Southern Territories", "Gabon", "Georgia", "Germany",
            "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada",
            "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
            "Guyana", "Haiti", "Heard Island and McDonald Islands",
            "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
            "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy",
            "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
            "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
            "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali",
            "Malta", "Marshall Islands", "Martinique", "Mauritania",
            "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
            "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique",
            "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
            "Netherlands Antilles", "New Caledonia", "New Zealand",
            "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island",
            "North Korea", "Northern Marianas", "Norway", "Oman", "Pakistan",
            "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
            "Philippines", "Pitcairn Islands", "Poland", "Portugal",
            "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda",
            "Sqo Tome and Principe", "Saint Helena", "Saint Kitts and Nevis",
            "Saint Lucia", "Saint Pierre and Miquelon",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino",
            "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone",
            "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa",
            "South Georgia and the South Sandwich Islands", "South Korea",
            "Spain", "Sri Lanka", "Sudan", "Suriname",
            "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
            "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
            "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
            "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
            "Turks and Caicos Islands", "Tuvalu", "Uganda",
            "Ukraine", "United Arab Emirates", "United Kingdom",
            "United States", "United States Minor Outlying Islands",
            "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela",
            "Vietnam", "Virgin Islands", "Wallis and Futuna", "Western Sahara", "Yemen",
            "Yugoslavia", "Zambia", "Zimbabwe"};

}
