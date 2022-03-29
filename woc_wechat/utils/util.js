const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return `${[year, month, day].map(formatNumber).join('/')} ${[hour, minute, second].map(formatNumber).join(':')}`
}
const getDays = (endTime) => {
  var nowTime = formatTime(new Date());
  nowTime = Date.parse(nowTime);
  var ee = Date.parse(endTime);
  var days = parseInt((ee - nowTime) / (1000 * 60 * 60 * 24));
  if (days <= 0) {
    days = Math.abs(days) + 1;
  }
  return days;

}
const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}

module.exports = {
  formatTime
}
