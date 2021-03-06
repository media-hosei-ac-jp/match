import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SeminarComponent } from './seminar.component';

describe('SeminarComponent', () => {
  let component: SeminarComponent;
  let fixture: ComponentFixture<SeminarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeminarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeminarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
