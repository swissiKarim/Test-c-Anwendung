import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploaderViewComponent } from './uploader-view.component';

describe('UploaderViewComponent', () => {
  let component: UploaderViewComponent;
  let fixture: ComponentFixture<UploaderViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploaderViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploaderViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
