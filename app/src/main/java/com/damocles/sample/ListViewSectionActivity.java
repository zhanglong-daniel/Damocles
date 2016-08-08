package com.damocles.sample;

import com.baidu.naviauto.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewSectionActivity extends AppCompatActivity {

    private TextView mTopHeader;
    private ListView mListView;
    private int mTopVisiblePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_section);
        Utils.initToolbar(this, R.id.listview_section_toolbar);
        initViews();
    }

    private void initViews() {
        mTopHeader = (TextView) findViewById(R.id.listview_section_header);
        mListView = (ListView) findViewById(R.id.listview_section_list);
        mListView.setAdapter(new SectionAdapter(COUNTRIES));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem != mTopVisiblePosition) {
                    mTopVisiblePosition = firstVisibleItem;
                    setTopHeader(firstVisibleItem);
                }
            }
        });
        setTopHeader(0);
    }

    private void setTopHeader(int position) {
        mTopHeader.setText(COUNTRIES[position].substring(0, 1));
    }

    private class SectionAdapter extends ArrayAdapter<String> {

        public SectionAdapter(String[] objs) {
            super(ListViewSectionActivity.this, R.layout.activity_listview_section_item, R.id
                    .listview_section_list_item_label, objs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ListViewSectionActivity.this.getLayoutInflater().inflate(R.layout
                        .activity_listview_section_item, parent, false);
            }
            TextView header = (TextView) convertView.findViewById(R.id.listview_section_header);
            String label = getItem(position);
            // 当前item与前一item首字母不同，显示header
            if (position == 0 || getItem(position - 1).charAt(0) != label.charAt(0)) {
                header.setText(label.substring(0, 1));
                header.setVisibility(View.VISIBLE);
            } else {
                header.setVisibility(View.GONE);
            }
            return super.getView(position, convertView, parent);
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
