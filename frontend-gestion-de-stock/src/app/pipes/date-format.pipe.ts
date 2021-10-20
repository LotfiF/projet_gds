import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormatPipe'
})
export class DateFormatPipe implements PipeTransform {

  transform(value: string) {
    var datePipe = new DatePipe("en-US");
    const v = datePipe.transform(value, 'dd/MM/yyyy');
    return v;
 }

}
