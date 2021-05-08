export const USER_ID_KEY = "USER_ID";

export function setCookie(name, value, min) {
  const date = new Date();
  date.setMinutes(date.getMinutes() + min);
  const cookie_value = escape(value) + ((min == null) ? '' : '; expires='
      + date.toUTCString());
  document.cookie = name + '=' + cookie_value;
}

export function getCookie(name) {
  const value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
  return value ? value[2] : null;
}

export function deleteCookie(name) {
  const date = new Date();
  document.cookie = name + "= " + "; expires=" + date.toUTCString()
      + "; path=/";
}
