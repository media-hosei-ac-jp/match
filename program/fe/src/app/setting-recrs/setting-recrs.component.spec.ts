import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingRecrsComponent } from './setting-recrs.component';

describe('SettingRecrsComponent', () => {
  let component: SettingRecrsComponent;
  let fixture: ComponentFixture<SettingRecrsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingRecrsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingRecrsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
