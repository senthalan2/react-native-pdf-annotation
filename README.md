

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

Go to the [IOS Configuration Files](https://github.com/senthalan2/react-native-pdf-annotation/tree/main/ios) located in the repository and download all the files in the `MuPdfConfigFiles` directory using the [GitHub directory download](https://download-directory.github.io/) website or any other methods.

The downloaded files and directories are as follows: [Classes](https://github.com/senthalan2/react-native-pdf-annotation/tree/main/ios/MuPdfConfigFiles/Classes), [Libraries](https://github.com/senthalan2/react-native-pdf-annotation/tree/main/ios/MuPdfConfigFiles/Libraries), [Modules](https://github.com/senthalan2/react-native-pdf-annotation/tree/main/ios/MuPdfConfigFiles/Modules), [libmupdf](https://github.com/senthalan2/react-native-pdf-annotation/tree/main/ios/MuPdfConfigFiles/libmupdf), [build_libs.sh](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/ios/MuPdfConfigFiles/build_libs.sh), [common.h](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/ios/MuPdfConfigFiles/common.h), [common.m](https://github.com/senthalan2/react-native-pdf-annotation/blob/main/ios/MuPdfConfigFiles/common.m).

### Step 2

Move the downloaded `Classes` and `libmupdf` directories, as well as the `build_libs.sh`, `common.h`, and `common.m` files, to the `Your_Project_Name/ios` directory. Also, move the `PdfAnnotation.h` and `PdfAnnotation.mm` files, which are located in the downloaded Modules directory, to the `Your_Project_Name/ios` directory. Finally, your project's `ios` directory should look like the image below.

![1](https://github.com/user-attachments/assets/6617c95e-a717-4f98-bf03-bc37f1cdb72c)

### Step 3

Open your project in Xcode by opening `YourProjectName.xcworkspace` as shown in the image below.

![2](https://github.com/user-attachments/assets/23f057fa-a5a1-4056-ad30-9b9d61b303d9)

### Step 4

Drag and drop the `Classes` directory and the `common.h`, `common.m`, `PdfAnnotation.h` and `PdfAnnotation.mm` files into your project target in Xcode, as shown in the image below.

![3](https://github.com/user-attachments/assets/c5dfc397-01a6-4a61-b4a0-1c2387637de3)

### Step 5

Add the `libmupdf.a` and `libmupdfthird.a` files, which are located in the previously downloaded `Libraries` directory, to your project target under `Build Phases` -> `Link Binary With Libraries`, as shown in the image below.

![4](https://github.com/user-attachments/assets/31b6fd18-f2ac-46e4-91ec-75dfd56ba853)


### Step 6

In your project's `Targets` section, add another target by clicking the `add target (plus icon) button`. In the popup, select the `Other` tab and choose `External Build System` under the `Other` section. Click Next, enter `Generate` as the Product Name, and then click Finish. Refer to the video below for a demonstration of adding an External Build System target.

https://github.com/user-attachments/assets/ee988055-3b39-4fa8-8566-e712486943e1

### Step 7

Select the `Generate` target. Under `Info -> External Build Tool Configuration`, enter `generate` as the Arguments and choose the libmupdf directory path. In this case, the `libmupdf` directory is located inside the `ios` directory, so you should enter `libmupdf` as the Directory path, as shown in the image below.

![6](https://github.com/user-attachments/assets/bb1c9145-379a-414b-a1d6-674e4dad4a28)

### Step 8

Select your project's directory and open `Build Settings`. In `Build Settings`, search for `User Header Search Paths`. Add the mupdf library path as `libmupdf/include` to this section, as shown in the image below. The `libmupdf` directory was downloaded earlier and moved to `Your_Project_Name/ios`. The `include` directory, which contains the `mupdf library`, is located inside the `libmupdf` directory.

![7](https://github.com/user-attachments/assets/9f10e616-2303-40af-8ee3-c2927fbaf40c)


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

## Would you like to support me?

<a href="https://www.buymeacoffee.com/senthalan2" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-red.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>
