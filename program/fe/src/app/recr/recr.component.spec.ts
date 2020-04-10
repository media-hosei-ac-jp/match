import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecrComponent } from './recr.component';

describe('RecrComponent', () => {
  let component: RecrComponent;
  let fixture: ComponentFixture<RecrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecrComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
