package Deepak.Thakur.com.flickpick.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String mID;
    private String mTITLE;
    private String mMOVIEPOSTER;
    private String mPLOT;
    private String mRATING;
    private String mRELEASEDATE;
    private  String mSHOWS;


    public Movie(){
    }


    private Movie(Parcel in) {
        mID = in.readString();
        mTITLE = in.readString();
        mMOVIEPOSTER = in.readString();
        mPLOT = in.readString();
        mRATING = in.readString();
        mRELEASEDATE = in.readString();
        mSHOWS = in.readString();

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmId() {
        return mID;
    }

    public void setmId(String mId) {
        this.mID = mId;
    }

    public String getmTitle() {
        return mTITLE;
    }

    public void setmTitle(String mTitle) {
        this.mTITLE = mTitle;
    }

    public String getmMoviePoster() {
        return mMOVIEPOSTER;
    }

    public void setmMoviePoster(String mMoviePoster) {
        this.mMOVIEPOSTER = mMoviePoster;
    }

    public String getmPlot() {
        return mPLOT;
    }

    public void setmPlot(String mPlot) {
        this.mPLOT = mPlot;
    }

    public String getmRating() {
        return mRATING;
    }

    public void setmRating(String mRating) {
        this.mRATING = mRating;
    }
    public void setmShows(String mShows) {
        this.mSHOWS = mShows;
    }


    public String getmReleaseDate() {
        return mRELEASEDATE;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mRELEASEDATE = mReleaseDate;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mTITLE);
        dest.writeString(mMOVIEPOSTER);
        dest.writeString(mPLOT);
        dest.writeString(mRATING);
        dest.writeString(mRELEASEDATE);
        dest.writeString(mSHOWS);

    }
}
