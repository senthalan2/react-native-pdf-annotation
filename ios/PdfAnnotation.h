
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNPdfAnnotationSpec.h"

@interface PdfAnnotation : NSObject <NativePdfAnnotationSpec>
#else
#import <React/RCTBridgeModule.h>

@interface PdfAnnotation : NSObject <RCTBridgeModule>
#endif

@end
