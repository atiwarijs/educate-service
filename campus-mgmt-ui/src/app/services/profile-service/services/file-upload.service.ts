import { Injectable } from '@angular/core';

export interface UploadedFileData {
  base64: string;
  mimeType: string;
  fileName: string;
}

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  constructor() {}

  convertToBase64(file: File): Promise<UploadedFileData> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result as string;
        const base64 = result.split(',')[1];
        resolve({
          base64,
          mimeType: file.type,
          fileName: file.name,
        });
      };
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }
}
