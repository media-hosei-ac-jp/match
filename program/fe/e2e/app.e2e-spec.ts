import { Semv2Page } from './app.po';

describe('semv2 App', function() {
  let page: Semv2Page;

  beforeEach(() => {
    page = new Semv2Page();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
