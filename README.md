

# react-native-pdf-annotation

The ```react-native-pdf-annotation``` NPM package provides PDF annotation for Android and iOS using [MuPDF]((https://github.com/ArtifexSoftware/mupdf/tree/34e18d127a02146e3415b33c4b67389ce1ddb614)). This package offers features such as `drawing on PDF pages`, `underlining text`, `highlighting text`, and `striking out text` in PDFs. Annotations can only be made on locally downloaded PDFs, as MuPDF supports local PDFs only.

Therefore, you need to install a file download package like `rn-fetch-blob` or `react-native-blob-util`. Refer to the [`rn-fetch-blob`](https://www.npmjs.com/package/rn-fetch-blob) or [`react-native-blob-util`](https://www.npmjs.com/package/react-native-blob-util) package installation guides to download PDF files from remote URLs.


## Installation

```sh
npm install react-native-pdf-annotation
```
### Android

Go to your `android/app/src/main/AndroidManifest.xml` file and add the `MuPDFActivity` inside the application tag as shown below.

```xml
<application>
    ....
   <activity
        android:name=".MainActivity"
        android:label="@string/app_name"
        .... 
        >
        ....
      </activity>
      <activity android:name="com.artifex.mupdfdemo.MuPDFActivity" />   <!--   Add this   -->
</application>
```

### IOS

IOS is currently not fully supported by this package. However, we can implement it for iOS. See this [IOS Document](https://github.com/senthalan2/react-native-pdf-annotation?tab=readme-ov-file#ios-configuration).

## Usage

```js
import  *  as React from  'react';
import { 
openPdf, 
pdfEventEmitter, 
pdfEvents, 
type PageChangeDataProp, 
type PdfCloseDataProp, 
type PdfSaveCloseDataProp 
} from 'react-native-pdf-annotation';

// ...

function App() {
	// ...
	
	React.useEffect(()  => {
		const pageChangeEvent = pdfEventEmitter.addListener( 
			pdfEvents.onPdfPageChange, (event: PageChangeDataProp) => {
				console.log(event, pdfEvents.onPdfPageChange);
			}
		);
		const pdfCloseEvent = pdfEventEmitter.addListener(
			pdfEvents.onCloseWithSave, (event: PdfSaveCloseDataProp) => {
				console.log(event, pdfEvents.onCloseWithSave);
			}
		);
		const pdfSaveCloseEvent = pdfEventEmitter.addListener(
			pdfEvents.onCloseWithoutSave, (event: PdfCloseDataProp) => {
				console.log(event, pdfEvents.onCloseWithoutSave);
			}
		);
		return () => {
			pageChangeEvent.remove();
			pdfCloseEvent.remove();
			pdfSaveCloseEvent.remove();
		};
	}, []);
	
	// ...
	
	openPdf(filePath,  {
		isEnableAnnot:  true, // Show or Hide the annotation button
		continuePage: 2, // Continue Reading Page Number
		isEnableCustomFooterColor: true, // Enable/Disable Custom Footer Color (Android Only)
		isEnableCustomHeaderColor: true, // Enable/Disable Custom Header Color (Android Only)
	}).then((res)  =>  {
		console.log(res,  'Pdf Opened');
	}).catch((e)  =>  {
		console.log(e,  'open pdf error');
	});
	}

// ...

```
## Methods

| Method                            | Type     | Description                                              |
| --------------------------------- | -------- | -------------------------------------------------        |
| openPdf(filePath, config)         | Promise  | To open the downloaded PDF file for reading and annotation |

## Config
The first parameter of the `openPdf()` method is `filePath`, which must be a local file path of type `string`.

This is an Object Which you pass as second parameter to the `openPdf()` Method.

| Key                       | type    | Required | Default   | IOS                | Android            | Description                                                          |
| -----------------         | ------- | -------- | --------- | ------------------ | ------------------ | ---------------------------------------------------------------------|
| isEnableAnnot             | Boolean | No       |	true     | :white_check_mark: | :white_check_mark: | A boolean value that decides whether to enable annotation            |
| continuePage              | Number  | No       |	0        | :white_check_mark: | :white_check_mark: | The page number from which to continue reading the PDF               | 
| isEnableCustomHeaderColor | Boolean | No       | false     | :x:                | :white_check_mark: | A boolean value that decides whether to enable custom header color   | 
| isEnableCustomFooterColor | Boolean | No       | false     | :x:                | :white_check_mark: | A boolean value that decides whether to enable custom footer color   |

### Custom Header and Footer Color Android Configuration

Go to your `android/app/src/main/res/values`. If the `res` and `values` directories does not exist, then you have to create it and continue the below steps.
Open `colors.xml` file. If the `colors.xml` file does not exist, then you have to create it and continue the below steps.

Add Header Custom Color named pdf_header_background_color and Footer Custom Color named pdf_footer_background_color.

The `colors.xml` file looks like this:

```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
    <color name="pdf_header_background_color">#00FF00</color>
    <color name="pdf_footer_background_color">#0000FF</color>
  </resources>
```
Note: These colors are applied when the `isEnableCustomHeaderColor` and `isEnableCustomFooterColor` props are true accordingly.

## Events

### onPdfPageChange
This event is triggered whenever the page number changes. The returned data includes:

| Key        | Type     | Description                          |
| ---------- | -------- | ------------------------------------ |
| pageNumber | Number   | The current page number              |
| totalPages | Number   | The total number of pages in the PDF |


### onCloseWithSave
This event is triggered when the PDF changes and is saved upon closing. The returned data includes:

| Key         | Type     | Description                          |
| ----------- | -------- | ------------------------------------ |
| currentPage | Number   | The current page number              |
| totalPages  | Number   | The total number of pages in the PDF |
| filePath    | String   | The saved file path                  |


### onCloseWithoutSave
This event is triggered when the PDF is closed without making changes or saving. The returned data includes:

| Key         | Type     | Description                          |
| ----------  | -------- | ------------------------------------ |
| currentPage | Number   | The current page number              |
| totalPages  | Number   | The total number of pages in the PDF |


## IOS Configuration

### Step 1

Clone the [IOS Configuration Files](https://github.com/senthalan2/MuPdf_IOS_Configuration_Files) repository using the command below.

```sh
git clone https://github.com/senthalan2/MuPdf_IOS_Configuration_Files.git
```


### Step 2

Go to the `MuPdf_IOS_Configuration_Files` directory and move the `Classes` and `libmupdf` (located inside the `IOS_Library` directory) directories, as well as the `build_libs.sh`, `common.h`, and `common.m` files, to the `Your_Project_Name/ios` directory. Also, move the `PdfAnnotation.h` and `PdfAnnotation.mm` files (located in the `Modules` directory) to the `Your_Project_Name/ios` directory. Finally, your project's `ios` directory should look like the image below.

![Step_2](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_2.png)

### Step 3

Open your project in Xcode by opening `YourProjectName.xcworkspace` as shown in the image below.

![Step_3](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_3.png)

### Step 4

Drag and drop the `Classes` directory and the `common.h`, `common.m`, `PdfAnnotation.h` and `PdfAnnotation.mm` files into your project in Xcode, as shown in the image below.

![Step_4](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_4.png)

### Step 5

Add the `libmupdf.a` and `libmupdfthird.a` files, which are located in the `Libraries` directory of the cloned `MuPdf_IOS_Configuration_Files` repository, to your project target under `Build Phases -> Link Binary With Libraries`.

![Step_5](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_5.png)


### Step 6

In your project's `Targets` section, add another target by clicking the `add target (plus icon) button`. In the popup, select the `Other` tab and choose `External Build System` under the `Other` section. Click Next, enter `Generate` as the Product Name, and then click Finish. Refer to the video below for a demonstration of adding an External Build System target.

https://github.com/user-attachments/assets/5f77b75d-1ba7-4f40-92b2-d3e73208fc79

### Step 7

Select the `Generate` target. Under `Info -> External Build Tool Configuration`, enter `generate` as the Arguments and choose the `libmupdf` directory path. In this case, the `libmupdf` directory is located inside the `ios` directory, so you should enter `libmupdf` as the Directory path, as shown in the image below.

![Step_7](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_7.png)

### Step 8

Select your project's directory and open `Build Settings`. In `Build Settings`, search for `User Header Search Paths`. Add the mupdf library path as `libmupdf/include` to this section, as shown in the image below. The `libmupdf` directory was moved from the cloned repository `MuPdf_IOS_Configuration_Files` to `Your_Project_Name/ios`. The `include` directory, which contains the `mupdf` library, is located inside the `libmupdf` directory.

![Step_8.1](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_8.1.png)

In the same `Build Settings`, search for `Objective-C Automatic Reference Counting` and change its value to `No`, as shown in the image below.

![Step_8.2](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_8.2.png)

### Step 9

Select your project's target and open `Build Phases`. Click the plus button and choose `New Run Script Phase` from the menu to add a run script for libmupdf, as shown in the image below.

![Step_9](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_9.png)

### Step 10

In the `Run Script` section, enter the following script to run mupdf's build script. This script generates the supported files for mupdf.

```sh
bash build_libs.sh
```
In the `Run Script`, add the output files under the `Output Files` section, as shown in the image below.

![Step_10](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_10.png)

### Step 11

Drag the `Run Script` section from the bottom to above the `Link Binary With Libraries` and `Compile Sources` sections, as shown in the image below.

![Step_11](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_11.png)

### Step 12

That's it! We have integrated PDF Annotation in iOS. Now, clean the build folder and erase derived data.

##### Clean the Build Folder: 

In Xcode, go to the menu bar and select `Product -> Clean Build Folder`.

##### Erase Derived Data:

Go to `Xcode -> Preferences/Settings -> Locations` tab.
Click on the arrow next to the `Derived Data folder path` to open it in Finder.
Delete the contents of the Derived Data folder.

##### Follow the Below Steps:

Click the scheme menu and select `Generate`. Then, run the build, as shown in the image below.

![Step_12](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/assets/step_12.png)


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

## Would you like to support me?

<a href="https://www.buymeacoffee.com/senthalan2" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-red.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>
