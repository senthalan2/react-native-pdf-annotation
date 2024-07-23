import {
  DeviceEventEmitter,
  NativeEventEmitter,
  NativeModules,
  Platform,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-pdf-annotation' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PdfAnnotation = NativeModules.PdfAnnotation
  ? NativeModules.PdfAnnotation
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export interface PageChangeDataProp {
  pageNumber: number;
  totalPages: number;
}

export interface PdfCloseDataProp {
  currentPage: number;
  totalPages: number;
}

export interface PdfSaveCloseDataProp extends PdfCloseDataProp {
  filePath: string;
}

export interface PdfEventsProps {
  onPdfPageChange: PageChangeDataProp;
  onCloseWithSave: 'onCloseWithSave';
  onCloseWithoutSave: 'onCloseWithoutSave';
}

export interface PdfAnnotationOptions {
  /**
   * A boolean value that decides whether to enable annotation.
   */
  isEnableAnnot?: boolean;
  /**
   * The page number from which to continue reading the PDF.
   */
  continuePage?: number;
  /**
   * A boolean value that decides whether to enable custom header color.
   * This property is only applicable for Android.
   */
  isEnableCustomHeaderColor?: boolean;
  /**
   * A boolean value that decides whether to enable custom footer color.
   * This property is only applicable for Android.
   */
  isEnableCustomFooterColor?: boolean;
}

export const pdfEvents = {
  onPdfPageChange: 'onPdfPageChange',
  onCloseWithSave: 'onCloseWithSave',
  onCloseWithoutSave: 'onCloseWithoutSave',
};

export function openPdf(
  url: string,
  config?: PdfAnnotationOptions
): Promise<number> {
  return PdfAnnotation.openPdf(url, config);
}

export const pdfEventEmitter =
  Platform.OS === 'ios'
    ? new NativeEventEmitter(PdfAnnotation)
    : DeviceEventEmitter;
