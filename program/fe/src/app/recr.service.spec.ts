/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RecrService } from './recr.service';

describe('RecrService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RecrService]
    });
  });

  it('should ...', inject([RecrService], (service: RecrService) => {
    expect(service).toBeTruthy();
  }));
});
