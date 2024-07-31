import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Platform,
} from 'react-native';
import ReactNativeBlobUtil, {
  type ReactNativeBlobUtilConfig,
} from 'react-native-blob-util';
import {
  openPdf,
  pdfEventEmitter,
  pdfEvents,
  type PageChangeDataProp,
  type PdfCloseDataProp,
  type PdfSaveCloseDataProp,
} from 'react-native-pdf-annotation';

export default function App() {
  const IS_IOS = Platform.OS === 'ios';
  React.useEffect(() => {
    const pageChangeEvent = pdfEventEmitter.addListener(
      pdfEvents.onPdfPageChange,
      (event: PageChangeDataProp) => {
        console.log(event, pdfEvents.onPdfPageChange);
      }
    );
    const pdfCloseEvent = pdfEventEmitter.addListener(
      pdfEvents.onCloseWithSave,
      (event: PdfSaveCloseDataProp) => {
        console.log(event, pdfEvents.onCloseWithSave);
      }
    );
    const pdfSaveCloseEvent = pdfEventEmitter.addListener(
      pdfEvents.onCloseWithoutSave,
      (event: PdfCloseDataProp) => {
        console.log(event, pdfEvents.onCloseWithoutSave);
      }
    );
    return () => {
      pageChangeEvent.remove();
      pdfCloseEvent.remove();
      pdfSaveCloseEvent.remove();
    };
  }, []);

  const getDirectoryFilePath = (fileName: string) => {
    const { fs } = ReactNativeBlobUtil;

    let downloadDir = IS_IOS ? fs.dirs.DocumentDir : fs.dirs.DownloadDir;

    return downloadDir + `/PDFAnnotation/` + fileName;
  };

  const handleDownload = () => {
    const url =
      'https://avrpt-vps.in/demo/uploads/pdfs/B0001_662739e73488a_662739e73488c.pdf';
    const { config } = ReactNativeBlobUtil;
    const fileName = 'book.pdf';
    const filePath = getDirectoryFilePath(fileName);
    let options: ReactNativeBlobUtilConfig = {
      fileCache: true,
      path: filePath,
      addAndroidDownloads: {
        useDownloadManager: true,
        notification: true,
        path: filePath,
        description: `File Download`,
        title: fileName,
      },
    };
    config(options)
      .fetch('GET', url)
      .then(() => {
        openPdf(filePath, {
          // isEnableBookMark: false,
          continuePage: 1,
          // isEnableCustomFooterColor: true,
          // isEnableAnnot: true,
          // isEnableCustomHeaderColor: true,
          // continuePage: 0,
        })
          .then((res) => {
            console.log(res, 'RESPONSE');
          })
          .catch((e) => {
            console.log(e, 'ERROR');
          });
        console.log(filePath, 'FILEPATH');
      })
      .catch((e) => {});
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={{ padding: 10, borderRadius: 10, backgroundColor: 'tomato' }}
        activeOpacity={0.8}
        onPress={() => {
          handleDownload();
        }}
      >
        <Text style={{ color: 'white' }}>Click here to download pdf</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
