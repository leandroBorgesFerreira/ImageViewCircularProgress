[ ![Download](https://api.bintray.com/packages/lehen01/maven/ImageViewCircularProgress/images/download.svg) ](https://bintray.com/lehen01/maven/ImageViewCircularProgress/_latestVersion)

## ImageViewCircularProgress

ImageView with a circular progress spinner

Android Button that morphs into a loading progress bar. 
  - Really simple to use.
  - Progress spinner out-of-box. You don't have to created a progress on top of a ImageView

![enter image description here](https://media.giphy.com/media/3o7btRuntGxVq14Joc/giphy.gif)

## Usage
Put the xml in the layout    

    <br.com.simplepass.circularprogressimagelib.CircularProgressImageView
                android:id="@+id/progress_image"
                android:layout_width="200dp"
                android:layout_height="200dp"
                android:src="@mipmap/ic_launcher"
                app:loading_spinning_bar_color="@android:color/white"
                app:loading_spinning_bar_width="4dp"
                app:loading_spinning_bar_padding="80dp"
                />


And use it

    CircularProgressImageView progressImageView = (CircularProgressImageView)
                    findViewById(R.id.progress_image);
    [after some async task...]
    
    progressImageView.doneLoagingAnimation(R.color.colorAccent,
                                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

## Install

    compile 'br.com.simplepass:imageview-circular-progress:1.0.5'


## Bugs and Feedback


For bugs, feature requests, and discussion please use [GitHub Issues](https://github.com/leandroBorgesFerreira/ImageViewCircularProgress/issues)



> Written with [StackEdit](https://stackedit.io/).
